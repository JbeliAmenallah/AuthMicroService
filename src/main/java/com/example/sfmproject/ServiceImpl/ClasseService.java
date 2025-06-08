package com.example.sfmproject.ServiceImpl;

import com.example.sfmproject.Entities.Classe;
import com.example.sfmproject.Repositories.classeRepository;
import com.example.sfmproject.Services.IClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClasseService implements IClasseService {
    @Autowired
    classeRepository classeRepository;

    @Override
    public List<Classe> GetAllClasses() {
        return classeRepository.findAll();
    }

    @Override
    public Classe GetClassById(Long id) {
        return classeRepository.findById(id).get();
    }

    @Override
    public void AddClasse(Classe classe) {
  classeRepository.save(classe);
    }

    @Override
    public void UpdateClasse(Long id) {
classeRepository.save(classeRepository.findById(id).get());
    }

    @Override
    public void DeleteClasse(Long id) {
classeRepository.deleteById(id);
    }

    @Override
    public List<Classe> getClassesByNiveau(Long niveauId) {

        return classeRepository.findClassesByNiveauId(niveauId);
    }

    @Override
    public List<Classe> getClassesByTeacherId(Long teacherId) {
        return classeRepository.findTeachingClassesByUserId(teacherId);
    }
}
