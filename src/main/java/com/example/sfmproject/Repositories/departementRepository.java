package com.example.sfmproject.Repositories;

import com.example.sfmproject.Entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface departementRepository extends JpaRepository<Departement, Long> {
}
