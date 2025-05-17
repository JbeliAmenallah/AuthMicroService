package com.example.sfmproject.Controllers;

import com.example.sfmproject.Entities.Departement;
import com.example.sfmproject.Services.IDepartementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/departements")
public class DepartementController {

    @Autowired
    private IDepartementService departementService;

    // Get all departments
    @GetMapping
    public ResponseEntity<List<Departement>> getAllDepartements() {
        List<Departement> departements = departementService.getAllDepartement();
        return ResponseEntity.ok(departements);
    }

    // Get a department by ID
    @GetMapping("/{id}")
    public ResponseEntity<Departement> getDepartementById(@PathVariable Long id) {
        Departement departement = departementService.getDepartementById(id);
        return departement != null ? ResponseEntity.ok(departement) : ResponseEntity.notFound().build();
    }

    // Add a new department
    @PostMapping
    public ResponseEntity<Departement> addDepartement(@RequestBody Departement departement) {
        departementService.addDepartement(departement);
        return ResponseEntity.ok(departement);
    }

    // Update an existing department
    @PutMapping("/{id}")
    public ResponseEntity<Departement> updateDepartement(@PathVariable Long id, @RequestBody Departement updatedDepartement) {
        Departement existingDepartement = departementService.getDepartementById(id);

        if (existingDepartement != null) {
            existingDepartement.setNomDepartement(updatedDepartement.getNomDepartement());
           // existingDepartement.setOrganisation(updatedDepartement.getOrganisation());
            existingDepartement.setNiveaux(updatedDepartement.getNiveaux());
            departementService.updateDepartement(id);
            return ResponseEntity.ok(existingDepartement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a department
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable Long id) {
        departementService.deleteDepartement(id);
        return ResponseEntity.noContent().build();
    }
}
