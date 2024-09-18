package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.entities.Movie;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Daniel Rouvillain
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class MovieDTO {
    private String title;
    private String overview;
    private double lowerRating;
    private double upperRating;
    private List<MovieDTO> movies;
    private List<GenreDTO> genres;

    @JsonProperty("release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    public MovieDTO(Movie movie) {
    }

    public String getReleaseYear() {
        return releaseDate != null ? String.valueOf(releaseDate.getYear()) : null;
    }



}
