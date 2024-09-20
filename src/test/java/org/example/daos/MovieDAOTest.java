package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.config.HibernateConfig;
import org.example.entities.Movie;
import org.example.services.JsonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class MovieDAOTest {

    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final MovieDAO movieDAO = MovieDAO.getInstance(emf);
    private static Movie m1;
    private static Movie m2;
    private static Movie m3;


    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Movie").executeUpdate();
            em.createNativeQuery("DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'movie_id_seq') THEN CREATE SEQUENCE movie_id_seq; END IF; END $$;").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE movie_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();

            String jsonString = "{ \"id\": 833339, \"title\": \"Speak No Evil\", \"overview\": \"A Danish family visits a Dutch family they met on a holiday. What was supposed to be an idyllic weekend slowly starts unraveling as the Danes try to stay polite in the face of unpleasantness.\", \"releaseDate\": \"2022-03-17\", \"averageRating\": 6.594, \"popularity\": 59.287 }";
            m1 = JsonService.convertJsonToObject(jsonString, Movie.class);
            m1 = movieDAO.saveMovieForTest(m1);
            String m1jsonString = "{ \"id\": 980026, \"title\": \"The Promised Land\", \"overview\": \"Denmark, 1755. Captain Ludvig Kahlen sets out to conquer a Danish heath reputed to be uncultivable, with an impossible goal: to establish a colony in the name of the king, in exchange for a royal title. A single-minded ambition that the ruthless lord of the region will relentlessly seek to put down. Kahlen's fate hangs in the balance: will his endevours bring him wealth and honour, or cost him his life...?\", \"releaseDate\": \"2023-10-05\", \"averageRating\": 7.8, \"popularity\": 25.027 }";
            m2 = JsonService.convertJsonToObject(m1jsonString, Movie.class);
            m2 = movieDAO.saveMovieForTest(m2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @AfterEach
    void tearDown() {

    }
    @Test
    void saveMovie() {
     String jsonString1 = "{ \"id\": 519465, \"title\": \"Queen of Hearts\", \"overview\": \"Anne, a brilliant and dedicated advocacy lawyer specialising in society’s most vulnerable, children and young adults, lives what appears to be the picture-perfect life with her doctor-husband, Peter, and their twin daughters. When her estranged teenage stepson, Gustav, moves in with them, Anne’s escalating desire leads her down a dangerous rabbit hole which, once exposed, unleashes a sequence of events destined to destroy her world.\", \"releaseDate\": \"2019-03-27\", \"averageRating\": 6.956, \"popularity\": 32.82 }";
        m3 = JsonService.convertJsonToObject(jsonString1, Movie.class);
        m3 = movieDAO.saveMovieForTest(m3);
        assertNotNull(m3.getId());
        assertEquals(519465, m3.getId());
    }

    @Test
    void getAllMovies() {
        assertEquals(2, movieDAO.getAllMovies().size());
    }

    @Test
    void getMovieById() {
    }


    @Test
    void deleteMovie() {
    }

}