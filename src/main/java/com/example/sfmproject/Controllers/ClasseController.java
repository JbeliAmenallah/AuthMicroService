package com.example.sfmproject.Controllers;


import com.example.sfmproject.Entities.Classe;
import com.example.sfmproject.Entities.User;
import com.example.sfmproject.ServiceImpl.UserServiceIMP;
import com.example.sfmproject.Services.IClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClasseController {

    @Autowired
    private IClasseService classeService;

    @Autowired
    private UserServiceIMP userServiceIMP;

    // Get all classes
    @GetMapping
    public ResponseEntity<List<Classe>> getAllClasses() {
        List<Classe> classes = classeService.GetAllClasses();
        return ResponseEntity.ok(classes);
    }

    // Get a class by ID
    @GetMapping("/{id}")
    public ResponseEntity<Classe> getClassById(@PathVariable Long id) {
        Classe classe = classeService.GetClassById(id);
        return ResponseEntity.ok(classe);
    }

    // Add a new class
    @PostMapping
    public ResponseEntity<Classe> addClasse(@RequestBody Classe classe) {
        classeService.AddClasse(classe);
        return ResponseEntity.ok(classe);
    }

    // Update an existing class
    @PutMapping("/{id}")
    public ResponseEntity<Classe> updateClasse(@PathVariable Long id, @RequestBody Classe updatedClasse) {
        Classe existingClasse = classeService.GetClassById(id);

        if (existingClasse != null) {
            existingClasse.setNomClasse(updatedClasse.getNomClasse());
            existingClasse.setNiveau(updatedClasse.getNiveau());
            classeService.UpdateClasse(id);
            return ResponseEntity.ok(existingClasse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a class
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClasse(@PathVariable Long id) {
        classeService.DeleteClasse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/niveau/{niveauId}")
    public ResponseEntity<List<Classe>> getClassesByNiveau(@PathVariable Long niveauId) {
        List<Classe> classes = classeService.getClassesByNiveau(niveauId);
        return ResponseEntity.ok(classes);
    }

    // Get classes by teacher ID
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Classe>> getClassesByTeacherId(@PathVariable Long teacherId) {
        List<Classe> classes = classeService.getClassesByTeacherId(teacherId);
        return ResponseEntity.ok(classes);
    }




}