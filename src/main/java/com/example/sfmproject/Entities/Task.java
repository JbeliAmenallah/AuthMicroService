package com.example.sfmproject.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "repository_id")
    private Repository repository;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo; // étudiant

    private Double grade; // note
    private String comment; // commentaire
}
