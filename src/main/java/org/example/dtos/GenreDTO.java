package org.example.dtos;

import lombok.Builder;
import lombok.Data;
import org.example.entities.Genre;

import java.util.List;

/**
 * Purpose:
 *
 * @author: Kevin Løvstad Schou
 */

@Data
@Builder
public class GenreDTO {

    private int id;

    private String name;

    private List<GenreDTO> genres;


    public GenreDTO(Genre genre){

    }

    public GenreDTO() {
    }

    public GenreDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public GenreDTO(int id, String name, List<GenreDTO> genres) {
        this.id = id;
        this.name = name;
        this.genres = genres;
    }

    public List<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDTO> genres) {
        this.genres = genres;
    }


    @Override
    public String toString() {
        return "GenreDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
