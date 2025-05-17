package com.example.sfmproject.ServiceImpl;

import com.example.sfmproject.Repositories.niveauRepository;

import com.example.sfmproject.Entities.Niveau;
import com.example.sfmproject.Services.INiveauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NiveauService implements INiveauService {
    @Autowired
    niveauRepository niveauRepository;

    @Override
    public List<Niveau> getNiveau() {
        return niveauRepository.findAll();
    }

    @Override
    public Niveau getNiveauById(Long id) {
        return niveauRepository.findById(id).get();
    }

    @Override
    public void updateNiveau(Long id) {
    niveauRepository.save(getNiveauById(id));
    }

    @Override
    public void deleteNiveau(Long id) {
    niveauRepository.deleteById(id);
    }

    @Override
    public void addNiveau(Niveau niveau) {
        niveauRepository.save(niveau);
    }
}
