package com.example.sfmproject.Services;


import com.example.sfmproject.Entities.Departement;

import java.util.List;

public interface IDepartementService {
    List<Departement> getAllDepartement();
    Departement getDepartementById(Long id);
    void addDepartement(Departement departement);
    void updateDepartement(Long id);
    void deleteDepartement(Long id);

    List<Departement> getDepartmentsByOrganisation(Long orgId);

}
