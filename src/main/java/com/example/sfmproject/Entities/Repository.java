package com.example.sfmproject.Entities;

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
    @JoinTable(
            name = "repository_collaborators",
            joinColumns = @JoinColumn(name = "repository_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> collaborators = new HashSet<>();
}