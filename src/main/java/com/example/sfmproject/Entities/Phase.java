package com.example.sfmproject.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long grade; // Grade for the phase
    private String description; // Description of the phase

    @ManyToMany
    private Set<Repository> repositories = new HashSet<>(); // Repositories linked to this phase

    @OneToMany(mappedBy = "phase")
    @JsonIgnore
    private Set<Task> tasks = new HashSet<>(); // Tasks linked to this phase
}