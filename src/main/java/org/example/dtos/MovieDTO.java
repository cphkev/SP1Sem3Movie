package org.example.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {

    private String title;
    private String overview;
    private LocalDate releaseDate;
    private double averageRating;
    private double popularity;
    private Set<ActorDTO> actors;
    private Set<GenreDTO> genres;
    private DirectorDTO director;


    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

}
