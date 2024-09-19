package org.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "directors")
public class Director {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(mappedBy = "director",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Movie> movies;
}
