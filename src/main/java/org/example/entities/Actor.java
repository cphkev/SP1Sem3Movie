package org.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "actors")
public class Actor {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToMany(mappedBy = "actors", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Movie> movies;

}
