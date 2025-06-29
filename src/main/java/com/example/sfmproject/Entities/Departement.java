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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDepartement;

    private String nomDepartement;

    @OneToOne(mappedBy = "departement")
    @JsonIgnore
    private User chef; // Le chef de ce département

    @ManyToOne
    @JsonIgnore
    private Organisation organisation;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Niveau> niveaux = new ArrayList<>();
}
