package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dtos.GenreDTO;

import java.util.Set;

/**
 * Purpose:
 *
 * @author: Kevin Løvstad Schou
 */
@Data
@NoArgsConstructor
@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)//Might delete this
    private int id;

    @Column(name = "api_id", nullable = false, unique = true)
    private int apiId;

    @Column(name = "genre", nullable = false)
    private String genre;

    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies;

    public Genre(GenreDTO genreDTO) {
        this.id = genreDTO.getId();
        this.genre = genreDTO.getName();
    }

    public Genre(Genre genre) {
        this.id = genre.getId();
        this.apiId = genre.getApiId();
        this.genre = genre.getGenre();

    }
}
