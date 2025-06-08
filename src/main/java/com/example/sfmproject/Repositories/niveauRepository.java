package com.example.sfmproject.Repositories;

import com.example.sfmproject.Entities.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface niveauRepository extends JpaRepository<Niveau, Long> {
    @Query("SELECT n FROM Niveau n WHERE n.department.idDepartement = :deptId")
    List<Niveau> findNiveauxByDepartmentId(@Param("deptId") Long deptId);

}
