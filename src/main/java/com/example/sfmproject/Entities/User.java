package com.example.sfmproject.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String address;
    private int number;
    private boolean blocked;
    private boolean valid;
    private String token;
    private String image;
    private String githubToken; // New field for GitHub access token

    @OneToOne
    @JoinColumn(name = "departement_id")
    private Departement departement; // Chef de ce département

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude

    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "class_id")
    private Classe classEntity; // For students

    @ManyToMany
    @JoinTable(
            name = "teacher_classes",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "class_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"teacher_id", "class_id"})
    )
    private Set<Classe> teachingClasses = new HashSet<>();


    @OneToMany(mappedBy = "assignedTo")
    @JsonIgnore
    private List<Task> assignedTasks = new ArrayList<>(); // Tasks assigned to this user

    @OneToMany(mappedBy = "creator")
    @JsonIgnore
    private List<Repository> createdRepositories = new ArrayList<>(); // Repositories created by this user

    @ManyToMany(mappedBy = "collaborators")
    @JsonIgnore
    private List<Repository> repositories = new ArrayList<>(); // Repositories this user collaborates on

    public User(String name, String username, String email, String password, boolean blocked, String address, boolean valid) {

        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.blocked = blocked;
        this.address = address;
        this.valid = valid;

    }




}
