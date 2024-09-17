package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.Set;

/**
 * @author Daniel Rouvillain
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
    private Set<Genre> genre;
    public Movie() {
    }

    public Movie(String title, String overview, double lowerRating, double upperRating) {
        this.title = title;
        this.overview = overview;
        this.lowerRating = lowerRating;
        this.upperRating = upperRating;
    }

}
