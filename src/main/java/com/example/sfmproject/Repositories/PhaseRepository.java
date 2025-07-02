package com.example.sfmproject.Repositories;

import com.example.sfmproject.Entities.Classe;
import com.example.sfmproject.Entities.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;

import java.util.List;

public interface PhaseRepository extends JpaRepository<Phase, Long> {
    List<Phase> findByRepositories_Id(Long repositoryId); // Find phases by repository ID
}
