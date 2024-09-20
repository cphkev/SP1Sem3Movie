package org.example;

import jakarta.persistence.EntityManagerFactory;
import org.example.config.HibernateConfig;
import org.example.daos.MovieDAO;
import org.example.dtos.MovieDTO;
import org.example.services.MovieService;

import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("sp1movie");
        MovieDAO movieDAO = new MovieDAO(emf);
        MovieService movieService = new MovieService(movieDAO);

        List<MovieDTO> danishMovies = movieService.getDanishMoviesFromLastFiveYears();
        for(MovieDTO movieDTO : danishMovies) {
            movieService.addMovie(movieDTO);
        }

        List<MovieDTO> allMovies = movieService.getAllMovies();
        allMovies.forEach(move -> System.out.println(move.getTitle()));




    }
}