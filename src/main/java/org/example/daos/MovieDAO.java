package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.example.dtos.MovieDTO;
import org.example.entities.Movie;
import org.example.services.MovieService;

import java.util.List;

public class MovieDAO {

    private final EntityManagerFactory emf;
    private static MovieDAO instance;
    private static MovieService movieService;

    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static MovieDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new MovieDAO(emf);
        }
        return instance;
    }

    public List<Movie> getAllMovies() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT m FROM Movie m", Movie.class);
        return query.getResultList();
    }

    public Movie getMovieById(int id) {
        EntityManager em = emf.createEntityManager();
        return em.find(Movie.class, id);
    }

    public Movie saveMovie(Movie movie) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(movie);
        em.getTransaction().commit();
        em.close();
        return movie;
    }

    public Movie saveMovieForTest(Movie movie) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(movie);
        em.getTransaction().commit();
        em.close();
        return movie;
    }

    public void deleteMovie(int id) {
        EntityManager em = emf.createEntityManager();
        Movie movie = em.find(Movie.class, id);
        if(movie != null) {
            em.getTransaction().begin();
            em.remove(movie);
            em.getTransaction().commit();
        }
        em.close();
    }

}
