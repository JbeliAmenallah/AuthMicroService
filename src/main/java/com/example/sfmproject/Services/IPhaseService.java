package com.example.sfmproject.Services;

import com.example.sfmproject.Entities.Phase;
import com.example.sfmproject.Entities.Repository;

import java.util.List;

public interface IPhaseService {

        Phase createPhase(Phase phase);
        Phase updatePhase(Long id, Phase phase);
        void deletePhase(Long id);
        Phase getPhaseById(Long id);
        List<Phase> getAllPhases();
        void assignPhaseToRepository(Long phaseId, Long repoId);
        public void assignAverageGradeToPhase(Long phaseId, Long repoId);
}
