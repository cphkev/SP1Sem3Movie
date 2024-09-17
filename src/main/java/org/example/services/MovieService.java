package org.example.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.dtos.MovieDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Rouvillain
 */

public class MovieService {
    private static String API_KEY = System.getenv("API_KEY");

    private static String BASE_URL = "https://api.themoviedb.org/3";
    private static String BASE_URL_MOVIE = "https://api.themoviedb.org/3/movie/";
    private static String BASE_URL_SEARCH = "https://api.themoviedb.org/3/search/movie";
    ObjectMapper objectMapper = new ObjectMapper();


    public MovieDTO getMovieById(int id) throws IOException, InterruptedException {
        String url = BASE_URL_MOVIE + id + "?api_key=" + API_KEY;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper.readValue(response.body(), MovieDTO.class);
    }


    public List<MovieDTO> getMoviesByRating(double lowerRating, double upperRating) throws IOException, InterruptedException {
        String url = BASE_URL + "/discover/movie?api_key=" + API_KEY + "&vote_average.gte=" + lowerRating + "&vote_average.lte=" + upperRating;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode jsonRespons = mapper.readTree(response.body());
        List<MovieDTO> movies = new ArrayList<>();

        JsonNode results = jsonRespons.get("results");

        if (results.isArray()) {
            for (JsonNode movieNode : results) {
                MovieDTO movie = new MovieDTO();
                movie.setTitle(movieNode.get("title").asText());

                movie.setOverview(movieNode.get("overview").asText());

                if (movieNode.has("release_date") && !movieNode.get("release_date").isNull()) {
                    movie.setReleaseDate(LocalDate.parse(movieNode.get("release_date").asText()));
                }
                movies.add(movie);
            }

        }
        return movies;
    }


    public List<MovieDTO> getDanishMovies() throws IOException, InterruptedException {
        List<MovieDTO> movies = new ArrayList<>();
        int page = 1;
        boolean morePages = true;

        LocalDate fiveYearsAgo = LocalDate.now().minusYears(5);
        String fiveYearsAgoStr = fiveYearsAgo.format(DateTimeFormatter.ISO_LOCAL_DATE);


        while (morePages) {
            String url = BASE_URL + "/discover/movie?api_key=" + API_KEY + "&with_origin_country=DK&page=" + page + "&primary_release_date.gte=" + fiveYearsAgoStr;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode jsonResponse = objectMapper.readTree(response.body());
            JsonNode results = jsonResponse.get("results");

            if (results.isArray()) {
                for (JsonNode movieNode : results) {
                    MovieDTO movie = new MovieDTO();
                    movie.setTitle(movieNode.get("title").asText());
                    movie.setOverview(movieNode.get("overview").asText());
                    if (movieNode.has("release_date") && !movieNode.get("release_date").isNull() && !movieNode.get("release_date").asText().isEmpty()) {
                        movie.setReleaseDate(LocalDate.parse(movieNode.get("release_date").asText()));
                    }
                    movies.add(movie);
                }
            }
            //print total amount of movies as int




            int totalPages = jsonResponse.get("total_pages").asInt();
            if (page >= totalPages) {
                morePages = false;
            } else {
                page++;
            }
        }

        return movies;
    }

    /*
        public static void main (String[]args){
                MovieService movieService = new MovieService();
                try {
                   List<MovieDTO> movies = movieService.getMoviesByRating(7.5, 8.0);
                    movies.forEach(System.out::println);
                    } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    */




    public static void main(String[] args) {
        MovieService movieService = new MovieService();
        try {
            List<MovieDTO> danishMovies = movieService.getDanishMovies();
            danishMovies.forEach(System.out::println);
            int totalMovies = danishMovies.size();
            System.out.println("Total amount of movies: " + totalMovies);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}

