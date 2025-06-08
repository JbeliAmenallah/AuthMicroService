package com.example.sfmproject.Repositories;

import com.example.sfmproject.Entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface departementRepository extends JpaRepository<Departement, Long> {

    @Query("SELECT d FROM Departement d")
    List<Departement> findAllDepartments();
    @Query("SELECT d FROM Departement d WHERE d.organisation.idOrganisation = :orgId")
    List<Departement> findDepartmentsByOrganisationId(@Param("orgId") Long orgId);

}
