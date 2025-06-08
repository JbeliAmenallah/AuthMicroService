package com.example.sfmproject.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClasse;

    private String nomClasse;

    @ManyToOne
    @JsonIgnore
    private Niveau niveau;

    @ManyToMany(mappedBy = "teachingClasses", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<User> teachers = new ArrayList<>();

    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<User> students = new ArrayList<>();
}
