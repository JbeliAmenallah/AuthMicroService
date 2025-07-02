package com.example.sfmproject.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private LocalDate dueDate;
    private String title;
    private String description;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "repository_id")
    @JsonIgnore
    private Repository repository; // Task belongs to a specific repository

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    @JsonIgnore
    private User assignedTo; // User assigned to this task

    @ManyToOne
    private Phase phase; // Task belongs to a specific phase

    private Double grade; // Grade for the task
    private String comment; // Comment on the task
}