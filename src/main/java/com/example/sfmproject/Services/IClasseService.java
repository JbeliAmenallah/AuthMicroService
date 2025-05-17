package com.example.sfmproject.Services;



import com.example.sfmproject.Entities.Classe;

import java.util.List;

public interface IClasseService {
    List<Classe> GetAllClasses();
    Classe GetClassById(Long id);

    void AddClasse(Classe classe);
    void UpdateClasse(Long id);
    void DeleteClasse(Long id);


}
