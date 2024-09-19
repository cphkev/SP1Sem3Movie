package org.example.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.daos.MovieDAO;
import org.example.dtos.ActorDTO;
import org.example.dtos.DirectorDTO;
import org.example.dtos.GenreDTO;
import org.example.dtos.MovieDTO;
import org.example.entities.Actor;
import org.example.entities.Director;
import org.example.entities.Genre;
import org.example.entities.Movie;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MovieService {

    private static final String API_KEY = System.getenv("API_KEY");
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private final MovieDAO movieDAO;

    public MovieService(MovieDAO movieDAO) {

        this.movieDAO = movieDAO;
    }

    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieDAO.getAllMovies();
        return movies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public MovieDTO getMovieById(int id) {
        Movie movie = movieDAO.getMovieById(id);
        return convertToDTO(movie);
    }

    public MovieDTO addMovie(MovieDTO movieDTO) {
        Movie movie = convertToEntity(movieDTO);
        movieDAO.saveMovie(movie);
        return convertToDTO(movie);
    }
    public void deleteMovie(int id) {

        movieDAO.deleteMovie(id);
    }
    public List<MovieDTO> getDanishMoviesFromLastFiveYears() throws Exception {

        LocalDate currentDate = LocalDate.now();
        LocalDate fiveYearsAgo = currentDate.minusYears(5);

        String endDate = currentDate.toString();
        String startDate = fiveYearsAgo.toString();

        List<MovieDTO> movies = new ArrayList<>();
        int page = 1;
        int totalPages = 1;


        while (page <= totalPages) {

            String url = BASE_URL + "/discover/movie?api_key=" + API_KEY +
                    "&with_origin_country=DK" +
                    "&primary_release_date.gte=" + startDate +
                    "&primary_release_date.lte=" + endDate +
                    "&page=" + page;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            JsonNode jsonResponse = mapper.readTree(response.body());

            totalPages = jsonResponse.get("total_pages").asInt();

            JsonNode results = jsonResponse.get("results");
            if (results.isArray()) {
                for (JsonNode movieNode : results) {
                    MovieDTO movie = new MovieDTO();
                    movie.setTitle(movieNode.get("title").asText());
                    movie.setOverview(movieNode.get("overview").asText());

                    if (movieNode.has("release_date") && !movieNode.get("release_date").isNull()) {
                        movie.setReleaseDate(LocalDate.parse(movieNode.get("release_date").asText()));
                    }

                    Set<GenreDTO> genres = fetchMovieGenres(movieNode.get("id").asInt());
                    movie.setGenres(genres);

                    Set<ActorDTO> actors = fetchMovieActors(movieNode.get("id").asInt());
                    movie.setActors(actors);

                    DirectorDTO director = fetchMovieDirector(movieNode.get("id").asInt());
                    movie.setDirector(director);

                    movies.add(movie);
                }
            }

            page++;
        }

        return movies;
    }

    private Set<GenreDTO> fetchMovieGenres(int movieId) throws Exception {
        String url = BASE_URL + "/movie/" + movieId + "?api_key=" + API_KEY;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode movieDetails = mapper.readTree(response.body());
        Set<GenreDTO> genres = new HashSet<>();

        if(movieDetails.has("genres") && movieDetails.get("genres").isArray()) {
            for (JsonNode genreNode : movieDetails.get("genres")) {
                GenreDTO genreDTO = new GenreDTO();
                genreDTO.setName(genreNode.get("name").asText());
                genres.add(genreDTO);
            }
        }
        return genres;
    }

    private Set<ActorDTO> fetchMovieActors(int movieId) throws Exception {
        String url = BASE_URL + "/movie/" + movieId + "/credits?api_key=" + API_KEY;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode movieCredits = mapper.readTree(response.body());
        Set<ActorDTO> actors = new HashSet<>();

        if(movieCredits.has("cast") && movieCredits.get("cast").isArray()) {
            for(JsonNode actorNode : movieCredits.get("cast")) {
                ActorDTO actorDTO = new ActorDTO();
                actorDTO.setName(actorNode.get("name").asText());
                actors.add(actorDTO);
            }
        }
        return actors;
    }

    private DirectorDTO fetchMovieDirector(int movieId) throws Exception {
        String url = BASE_URL + "/movie/" + movieId + "/credits?api_key=" + API_KEY;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode movieCredits = mapper.readTree(response.body());
        DirectorDTO director = new DirectorDTO();

        if(movieCredits.has("crew") && movieCredits.get("crew").isArray()) {
            for(JsonNode crewMember :movieCredits.get("crew")) {
                if(crewMember.get("job").asText().equals("Director")) {
                    director.setName(crewMember.get("name").asText());
                    break;
                }
            }
        }
        return director;
    }

    private MovieDTO convertToDTO(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setTitle(movie.getTitle());
        dto.setOverview(movie.getOverview());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setAverageRating(movie.getAverageRating());
        dto.setPopularity(movie.getPopularity());
        // Set genres
        dto.setGenres(movie.getGenres().stream().map(genre -> {
            GenreDTO genreDTO = new GenreDTO();
            genreDTO.setName(genre.getName());
            return genreDTO;
        }).collect(Collectors.toSet()));
        // Set actors
        dto.setActors(movie.getActors().stream().map(actor -> {
            ActorDTO actorDTO = new ActorDTO();
            actorDTO.setName(actor.getName());
            return actorDTO;
        }).collect(Collectors.toSet()));
        // Set director
        if(movie.getDirector() != null) {
            DirectorDTO directorDTO = new DirectorDTO();
            directorDTO.setName(movie.getDirector().getName());
            dto.setDirector(directorDTO);
        }
        return dto;
    }

    private Movie convertToEntity(MovieDTO dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setOverview(dto.getOverview());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setAverageRating(dto.getAverageRating());
        movie.setPopularity(dto.getPopularity());
        // Convert genres
        movie.setGenres(dto.getGenres().stream().map(genreDTO -> {
            Genre genre = new Genre();
            genre.setName(genreDTO.getName());
            return genre;
        }).collect(Collectors.toSet()));
        // Convert actors
        movie.setActors(dto.getActors().stream().map(actorDTO -> {
            Actor actor = new Actor();
            actor.setName(actorDTO.getName());
            return actor;
        }).collect(Collectors.toSet()));
        // Convert director
        if (dto.getDirector() != null) {
            Director director = new Director();
            director.setName(dto.getDirector().getName());
            movie.setDirector(director);
        }
        return movie;
    }

    public static void main(String[] args) throws Exception {

    }
}
