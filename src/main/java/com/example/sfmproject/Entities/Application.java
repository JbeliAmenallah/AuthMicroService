package com.example.sfmproject.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String clientId;

    @OneToMany(mappedBy = "application")
    private List<LogEntry> logs;

    @OneToMany(mappedBy = "application")
    private List<Task> tasks;

    // Getters and Setters
}
