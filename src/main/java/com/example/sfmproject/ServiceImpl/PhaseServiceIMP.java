// src/main/java/com/example/sfmproject/ServiceImpl/PhaseServiceIMP.java
package com.example.sfmproject.ServiceImpl;

import com.example.sfmproject.Entities.Phase;
import com.example.sfmproject.Entities.Repository;
import com.example.sfmproject.Entities.Task;
import com.example.sfmproject.Repositories.PhaseRepository;
import com.example.sfmproject.Repositories.RepositoryRepository;
import com.example.sfmproject.Repositories.TaskRepository;
import com.example.sfmproject.Services.IPhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhaseServiceIMP implements IPhaseService {
    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private RepositoryRepository repositoryRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Phase createPhase(Phase phase) {
        return phaseRepository.save(phase);
    }

    @Override
    public Phase updatePhase(Long id, Phase phase) {
        Phase existing = phaseRepository.findById(id).orElseThrow(() -> new RuntimeException("Phase not found"));
        existing.setName(phase.getName());
        existing.setDescription(phase.getDescription());
        // Ajoutez d'autres champs si besoin
        return phaseRepository.save(existing);
    }

    @Override
    public void deletePhase(Long id) {
        phaseRepository.deleteById(id);
    }

    @Override
    public Phase getPhaseById(Long id) {
        return phaseRepository.findById(id).orElseThrow(() -> new RuntimeException("Phase not found"));
    }

    @Override
    public List<Phase> getAllPhases() {
        return phaseRepository.findAll();
    }

    @Override
    public void assignPhaseToRepository(Long phaseId, Long repoId) {
        Phase phase = phaseRepository.findById(phaseId).orElseThrow(() -> new RuntimeException("Phase not found"));
        Repository repo = repositoryRepository.findById(repoId).orElseThrow(() -> new RuntimeException("Repository not found"));
        phase.getRepositories().add(repo);
        phaseRepository.save(phase);
    }

    @Override
    public void assignAverageGradeToPhase(Long phaseId) {
        Phase phase = phaseRepository.findById(phaseId).orElseThrow(() -> new RuntimeException("Phase not found"));

        double totalGrade = 0.0;
        int count = 0;

        for (Task task : phase.getTasks()) {
            if (task.getGrade() != null) { // Ensure grade is not null
                totalGrade += task.getGrade();
                count++;
            }
        }

        // Calculate and assign the average grade to the phase
        if (count > 0) {
            double averageGrade = totalGrade / count;
            phase.setGrade((long) averageGrade); // Assuming grade is of type Long
        } else {
            phase.setGrade(0L); // Set to 0 if no valid grades
        }

        phaseRepository.save(phase); // Persist the updated phase
    }


}