package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.example.dtos.MovieDTO;

import java.util.Set;

/**
 * @author Daniel Rouvillain, Kevin
 *
 */



@Data
@Entity

public class Movie {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "overview", nullable = false)
    private String overview;
    @Column(name = "lower_rating", nullable = false)
    private double lowerRating;
    @Column(name = "upper_rating", nullable = false)
    private double upperRating;


    @JoinColumn(name = "genre_id", nullable = false)

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    public Movie() {
    }

    public Movie(String title, String overview, double lowerRating, double upperRating) {
        this.title = title;
        this.overview = overview;
        this.lowerRating = lowerRating;
        this.upperRating = upperRating;
    }

    public Movie(MovieDTO movieDTO) {
        this.title = movieDTO.getTitle();
        this.overview = movieDTO.getOverview();
        this.lowerRating = movieDTO.getLowerRating();
        this.upperRating = movieDTO.getUpperRating();
    }
    public Movie(Movie movie) {
        this.title = movie.getTitle();
        this.overview = movie.getOverview();
        this.lowerRating = movie.getLowerRating();
        this.upperRating = movie.getUpperRating();
    }

}
