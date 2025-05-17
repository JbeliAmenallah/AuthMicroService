package com.example.sfmproject.Controllers;

import com.example.sfmproject.Entities.Niveau;
import com.example.sfmproject.Services.INiveauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/niveaux")
public class NiveauController {

    @Autowired
    private INiveauService niveauService;

    // Get all niveaux
    @GetMapping
    public ResponseEntity<List<Niveau>> getAllNiveaux() {
        List<Niveau> niveaux = niveauService.getNiveau();
        return ResponseEntity.ok(niveaux);
    }

    // Get a niveau by ID
    @GetMapping("/{id}")
    public ResponseEntity<Niveau> getNiveauById(@PathVariable Long id) {
        Niveau niveau = niveauService.getNiveauById(id);
        return niveau != null ? ResponseEntity.ok(niveau) : ResponseEntity.notFound().build();
    }

    // Add a new niveau
    @PostMapping
    public ResponseEntity<Niveau> addNiveau(@RequestBody Niveau niveau) {
        niveauService.addNiveau(niveau);
        return ResponseEntity.ok(niveau);
    }

    // Update an existing niveau
    @PutMapping("/{id}")
    public ResponseEntity<Niveau> updateNiveau(@PathVariable Long id, @RequestBody Niveau updatedNiveau) {
        Niveau existingNiveau = niveauService.getNiveauById(id);

        if (existingNiveau != null) {
            existingNiveau.setNomNiveau(updatedNiveau.getNomNiveau());
            //existingNiveau.setDepartements(updatedNiveau.getDepartements());
            existingNiveau.setClasses(updatedNiveau.getClasses());
            niveauService.updateNiveau(id);
            return ResponseEntity.ok(existingNiveau);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a niveau
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNiveau(@PathVariable Long id) {
        niveauService.deleteNiveau(id);
        return ResponseEntity.noContent().build();
    }
}