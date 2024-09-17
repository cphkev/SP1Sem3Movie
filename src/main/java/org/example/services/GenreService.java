package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import org.example.config.HibernateConfig;
import org.example.daos.GenreDAO;
import org.example.dtos.GenreDTO;
import org.example.dtos.MovieDTO;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Purpose:
 *
 * @author: Kevin Løvstad Schou
 */
public class GenreService {

    private static String API_KEY = System.getenv("API_KEY");

    private static List<GenreDTO> fetchMovieGenres() {
        String genreUrl = "https://api.themoviedb.org/3/genre/movie/list?api_key=" + API_KEY + "&language=en-US";
        List<GenreDTO> genreList = new ArrayList<>();

        try {
            // Send HTTP request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(genreUrl))
                    .build();


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the response using ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            GenreDTO genreResponse = objectMapper.readValue(response.body(), GenreDTO.class);

            // Populate the genreList with GenreDTO objects
            genreList.addAll(genreResponse.getGenres());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return genreList; // Return the list containing GenreDTOs
    }




    public static GenreDTO createGenre(int id, String name, EntityManagerFactory emf) {

        GenreDAO genreDAO = GenreDAO.getInstance(emf);

        GenreDTO genreDTO = GenreDTO.builder()
                .id(id)
                .name(name)
                .build();

        String json = JsonService.convertObjectToJson(genreDTO);
        return genreDTO = genreDAO.create(genreDTO);

    }



    public static void main(String[] args) {
/*
        MovieService movieService = new MovieService();


        List<GenreDTO> genreList = fetchMovieGenres();
        // List<MovieDTO> danishMovies = movieService.getDanishMovies();

        System.out.println("Genres:");
        genreList.forEach(System.out::println);

        System.out.println("\nDanish Movies:");
        // danishMovies.forEach(System.out::println);


 */

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("sp1movie");
        GenreDTO genreDTO = createGenre(1, "Action", emf);


    }
}
