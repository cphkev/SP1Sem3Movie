package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dtos.GenreDTO;

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
    @Column(name = "genre", nullable = false)
    private String genre;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public Genre(GenreDTO genreDTO) {
        this.id = genreDTO.getId();
        this.genre = genreDTO.getName();
    }

}
