package com.example.sfmproject.Repositories;

import com.example.sfmproject.Entities.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface classeRepository extends JpaRepository<Classe, Long> {
    @Query("SELECT c FROM Classe c WHERE c.niveau.idNiveau = :niveauId")
    List<Classe> findClassesByNiveauId(@Param("niveauId") Long niveauId);

    @Query("SELECT DISTINCT c FROM Classe c JOIN c.teachers t WHERE t.id = :teacherId")
    List<Classe> findClassesByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT c FROM Classe c JOIN c.teachers t WHERE t.id = :teacherId")
    List<Classe> findTeachingClassesByUserId(@Param("teacherId") Long teacherId);
}
