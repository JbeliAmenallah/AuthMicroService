package com.example.sfmproject.Controllers;

import com.example.sfmproject.Entities.Phase;
import com.example.sfmproject.Services.IPhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phases")
public class PhaseController {

    @Autowired
    private IPhaseService phaseService;

    @PostMapping
    public ResponseEntity<Phase> createPhase(@RequestBody Phase phase) {
        return ResponseEntity.ok(phaseService.createPhase(phase));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Phase> getPhaseById(@PathVariable Long id) {
        return ResponseEntity.ok(phaseService.getPhaseById(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Phase>> getAllPhases() {
        return ResponseEntity.ok(phaseService.getAllPhases());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Phase> updatePhase(@PathVariable Long id, @RequestBody Phase phase) {
        return ResponseEntity.ok(phaseService.updatePhase(id, phase));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhase(@PathVariable Long id) {
        phaseService.deletePhase(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{phaseId}/assign/{repoId}")
    public ResponseEntity<Void> assignPhaseToRepository(@PathVariable Long phaseId, @PathVariable Long repoId) {
        phaseService.assignPhaseToRepository(phaseId, repoId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{phaseId}/assign-average-grade/{repoId}")
    public ResponseEntity<Void> assignAverageGrade(@PathVariable Long phaseId, @PathVariable Long repoId ) {
        phaseService.assignAverageGradeToPhase(phaseId,repoId);
        return ResponseEntity.ok().build(); // Return 200 OK if successful
    }

}