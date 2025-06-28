package com.example.sfmproject.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Name of the phase
    private String description; // Description of the phase

    @ManyToMany
    private Set<Repository> repositories = new HashSet<>(); // Repositories linked to this phase
}