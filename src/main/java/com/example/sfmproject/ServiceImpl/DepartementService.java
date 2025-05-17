package com.example.sfmproject.ServiceImpl;

import com.example.sfmproject.Repositories.departementRepository;

import com.example.sfmproject.Entities.Departement;
import com.example.sfmproject.Services.IDepartementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartementService implements IDepartementService {
    @Autowired
    departementRepository departementRepository;

    @Override
    public List<Departement> getAllDepartement() {
        return departementRepository.findAll();
    }

    @Override
    public Departement getDepartementById(Long id) {
        return departementRepository.findById(id).get();
    }

    @Override
    public void addDepartement(Departement departement) {
    departementRepository.save(departement);
    }

    @Override
    public void updateDepartement(Long id) {
    departementRepository.save(departementRepository.findById(id).get());
    }

    @Override
    public void deleteDepartement(Long id) {
    departementRepository.deleteById(id);
    }
}
