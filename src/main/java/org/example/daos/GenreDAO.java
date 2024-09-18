package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import org.example.dtos.GenreDTO;
import org.example.entities.Genre;

/**
 * Purpose:
 *
 * @author: Kevin Løvstad Schou
 */
public class GenreDAO {

    private static GenreDAO instance;
    private static EntityManagerFactory emf;

    private GenreDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }



    public static GenreDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new GenreDAO(emf);
        }
        return instance;
    }



    public GenreDTO create(GenreDTO genreDTO) {
        Genre genre = new Genre(genreDTO);

        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(genre);
            em.getTransaction().commit();
        }
        return new GenreDTO(genre);
    }

    public GenreDTO createGenreWithoutDTO(Genre genreEntity) {
        Genre genre = new Genre(genreEntity);

        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(genre);
            em.getTransaction().commit();
        }
        return new GenreDTO(genre);
    }

//Not sure this works copilot made it
    public Genre findByApiId(int apiId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT g FROM Genre g WHERE g.apiId = :apiId", Genre.class)
                    .setParameter("apiId", apiId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }







}
