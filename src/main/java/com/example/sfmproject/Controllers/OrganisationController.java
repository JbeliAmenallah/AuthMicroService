package com.example.sfmproject.Controllers;

import com.example.sfmproject.Entities.*;
import com.example.sfmproject.ServiceImpl.UserServiceIMP;
import com.example.sfmproject.Services.IDepartementService;
import com.example.sfmproject.Services.IOrganisationService;
import com.example.sfmproject.Services.IDepartementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api/organisations")
public class OrganisationController {

    @Autowired
    private IOrganisationService organisationService;
    @Autowired
    private IDepartementService departementService;
    @Autowired
    UserServiceIMP userServiceIMP;


    // Get all organisations
    @GetMapping
    public ResponseEntity<List<Organisation>> getAllOrganisations() {
        List<Organisation> organisations = organisationService.getAllOrganisations();
        return ResponseEntity.ok(organisations);
    }

    // Get an organisation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Organisation> getOrganisationById(@PathVariable Long id) {
        Organisation organisation = organisationService.getOrganisationById(id);
        return organisation != null ? ResponseEntity.ok(organisation) : ResponseEntity.notFound().build();
    }

    // Add a new organisation
    @PostMapping
    public ResponseEntity<Organisation> addOrganisation(@RequestBody Organisation organisation) {
        organisationService.saveOrganisation(organisation);
        return ResponseEntity.ok(organisation);
    }

    // Update an existing organisation
    @PutMapping("/{id}")
    public ResponseEntity<Organisation> updateOrganisation(@PathVariable Long id, @RequestBody Organisation updatedOrganisation) {
        Organisation existingOrganisation = organisationService.getOrganisationById(id);

        if (existingOrganisation != null) {
            existingOrganisation.setNomOrganisation(updatedOrganisation.getNomOrganisation());
            existingOrganisation.setAddress(updatedOrganisation.getAddress());
            organisationService.updateOrganisation(id);
            return ResponseEntity.ok(existingOrganisation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete an organisation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganisation(@PathVariable Long id) {
        organisationService.deleteOrganisation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/hierarchy")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Organisation>> getHierarchyForAdmin() {
        return ResponseEntity.ok(organisationService.getAllDataForAdmin());
    }

    @GetMapping("/{orgId}/departments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Departement>> getDepartmentsByOrganisation(@PathVariable Long orgId) {
        List<Departement> departements = departementService.getDepartmentsByOrganisation(orgId);
        return ResponseEntity.ok(departements);
    }

//    @GetMapping("/hierarchy")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('enseignant') or hasRole('etudiant')")
//    public ResponseEntity<List<Organisation>> getOrganisationHierarchy() {
//        User currentUser = userServiceIMP.getCurrentUser()
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        System.out.println("Current User: " + currentUser.getUsername());
//        List<Organisation> organisations = organisationService.getAllOrganisations();
//        System.out.println("Organisations: " + organisations.size());
//
//        List<Organisation> userOrganisations = new ArrayList<>();
//
//        for (Organisation org : organisations) {
//            // Check if the user is an admin
//
//                userOrganisations.add(org);
//                System.out.println("User is admin, adding organisation: " + org.getNomOrganisation());
//                continue;
//
//        }
//        System.out.println("userOrganisations: " + userOrganisations.size());
//
//        return ResponseEntity.ok(userOrganisations);
//
//    }

//    @GetMapping("/hierarchy")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('enseignant') or hasRole('etudiant')")
//    public ResponseEntity<List<Organisation>> getOrganisationHierarchy() {
//        User currentUser = userServiceIMP.getCurrentUser()
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        System.out.println("Current User: " + currentUser.getUsername());
//        List<Organisation> organisations = organisationService.getAllOrganisations();
//        System.out.println("Organisations: " + organisations.size());
//
//        List<Organisation> userOrganisations = new ArrayList<>();
//
//        for (Organisation org : organisations) {
//            // Check if the user is an admin
//            if (currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("ADMIN"))) {
//                userOrganisations.add(org);
//                continue;
//            }
//
//            List<Departement> userDepartments = new ArrayList<>();
//            for (Departement dept : org.getDepartments()) {
//                System.out.println("Checking department: " + dept.getNomDepartement());
//
//                if (currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("enseignant"))) {
//                    // Fetch classes for the teacher
//                    List<Classe> classes = userServiceIMP.getClassesForTeacher(currentUser.getId());
//                    System.out.println("Classes for teacher: " + classes.size());
//
//                    // Check if any class is associated with the department
//                    if (dept.getNiveaux().stream()
//                            .flatMap(niveau -> niveau.getClasses().stream())
//                            .anyMatch(cls -> classes.stream().anyMatch(c -> c.getIdClasse().equals(cls.getIdClasse())))) {
//                        userDepartments.add(dept);
//                        System.out.println("Adding department for teacher: " + dept.getNomDepartement());
//                    }
//                } else if (currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("etudiant"))) {
//                    // Fetch the student's class
//                    Classe studentClass = userServiceIMP.getClassForStudent(currentUser.getId());
//                    System.out.println("Student Class: " + (studentClass != null ? studentClass.getNomClasse() : "None"));
//
//                    // Check if the student's class is associated with the department
//                    if (studentClass != null && dept.getNiveaux().stream()
//                            .flatMap(niveau -> niveau.getClasses().stream())
//                            .anyMatch(cls -> cls.getIdClasse().equals(studentClass.getIdClasse()))) {
//                        userDepartments.add(dept);
//                        System.out.println("Adding department for student: " + dept.getNomDepartement());
//                    }
//                }
//            }
//
//            if (!userDepartments.isEmpty()) {
//                org.setDepartments(userDepartments);
//                userOrganisations.add(org);
//                System.out.println("Adding organisation: " + org.getNomOrganisation());
//            }
//        }
//
//        return ResponseEntity.ok(userOrganisations);
//    }

    // src/main/java/com/example/sfmproject/Controllers/OrganisationController.java
    @GetMapping("/hierarchy")
    @PreAuthorize("hasRole('ADMIN') or hasRole('enseignant') or hasRole('etudiant')")
    public ResponseEntity<List<Organisation>> getOrganisationHierarchy() {
        User currentUser = userServiceIMP.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> roleNames = new ArrayList<>();
        for (Role role : currentUser.getRoles()) {
            roleNames.add(role.getRoleName().toString().toLowerCase());
        }
        System.out.println("Utilisateur: " + currentUser.getUsername() + " | Rôles: " + roleNames);

        List<Organisation> organisations = organisationService.getAllOrganisations();
        System.out.println("Organisations trouvées: " + organisations.size());

        if (roleNames.contains("admin")) {
            System.out.println("ADMIN: accès à toutes les organisations");
            return ResponseEntity.ok(organisations);
        }

        List<Organisation> userOrganisations = new ArrayList<>();

        for (Organisation org : organisations) {
            List<Departement> userDepartments = new ArrayList<>();
            for (Departement dept : org.getDepartments()) {
                System.out.println("Département: " + dept.getNomDepartement());
                if (roleNames.contains("enseignant")) {
                    List<Classe> classes = userServiceIMP.getClassesForTeacher(currentUser.getId());
                    System.out.println("Classes enseignant: " + classes.size());
                    boolean match = dept.getNiveaux().stream()
                            .flatMap(niveau -> niveau.getClasses().stream())
                            .anyMatch(cls -> classes.stream().anyMatch(c -> c.getIdClasse().equals(cls.getIdClasse())));
                    if (match) {
                        userDepartments.add(dept);
                        System.out.println("Ajout département pour enseignant: " + dept.getNomDepartement());
                    }
                } else if (roleNames.contains("etudiant")) {
                    Classe studentClass = userServiceIMP.getClassForStudent(currentUser.getId());
                    System.out.println("Classe étudiant: " + (studentClass != null ? studentClass.getNomClasse() : "Aucune"));
                    boolean match = studentClass != null && dept.getNiveaux().stream()
                            .flatMap(niveau -> niveau.getClasses().stream())
                            .anyMatch(cls -> cls.getIdClasse().equals(studentClass.getIdClasse()));
                    if (match) {
                        userDepartments.add(dept);
                        System.out.println("Ajout département pour étudiant: " + dept.getNomDepartement());
                    }
                }
            }
            if (!userDepartments.isEmpty()) {
                org.setDepartments(userDepartments);
                userOrganisations.add(org);
                System.out.println("Ajout organisation: " + org.getNomOrganisation());
            }
        }

        System.out.println("Total organisations retournées: " + userOrganisations.size());
        return ResponseEntity.ok(userOrganisations);
    }


}


