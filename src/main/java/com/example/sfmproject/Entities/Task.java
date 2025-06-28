package com.example.sfmproject.Entities;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

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
    private Repository repository; // Task belongs to a specific repository

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    @JsonIgnore
    private User assignedTo; // User assigned to this task

    private Double grade; // Grade for the task
    private String comment; // Comment on the task
}