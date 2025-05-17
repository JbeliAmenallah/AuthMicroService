package com.example.sfmproject.Services;


import com.example.sfmproject.Entities.Niveau;

import java.util.List;

public interface INiveauService {
    List<Niveau> getNiveau();
    Niveau getNiveauById(Long id);
    void updateNiveau(Long id);
    void deleteNiveau(Long id);
    void addNiveau(Niveau niveau);


}
