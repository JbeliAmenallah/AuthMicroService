package com.example.sfmproject.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repository {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String githubName;
    private String githubUrl;
    private String description;
    private Date createdAt;

    @ManyToOne
    private Classe classe;

    @ManyToOne
    private User creator; // Professeur

    @ManyToMany
    private Set<User> collaborators = new HashSet<>();


    @ManyToMany(mappedBy = "repositories")
    private Set<Phase> phases = new HashSet<>(); // Phases linked to this repository
}