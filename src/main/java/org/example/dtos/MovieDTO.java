package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

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

    @JsonProperty("release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    public String getReleaseYear() {
        return releaseDate != null ? String.valueOf(releaseDate.getYear()) : null;
    }



}
