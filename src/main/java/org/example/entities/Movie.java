package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.Set;

@Data
@Entity
@Table(name = "movies")
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

    @ManyToMany
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
}
