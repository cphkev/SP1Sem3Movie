package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.dtos.GenreDTO;
import org.example.dtos.MovieDTO;
import org.example.entities.Genre;
import org.example.entities.Movie;

/**
 * @author Daniel Rouvillain
 */

public class MovieDAO {
    private static MovieDAO instance;
    private static EntityManagerFactory emf;


    private MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }



    public static MovieDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new MovieDAO(emf);
        }
        return instance;
    }


//Not sure this works
    public MovieDTO create(Movie movieEntity) {
        Movie movie = new Movie(movieEntity);

        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(movie);
            em.getTransaction().commit();
        }
        return new MovieDTO(movie);
    }
}
