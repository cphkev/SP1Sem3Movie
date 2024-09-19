package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.example.entities.Movie;

import java.util.List;

public class MovieDAO {

    private final EntityManagerFactory emf;

    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
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
