package com.example.sfmproject.ServiceImpl;

import com.example.sfmproject.Entities.Repository;
import com.example.sfmproject.Entities.User;
import com.example.sfmproject.Repositories.RepositoryRepository;
import com.example.sfmproject.Services.IRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepositoryServiceIMP implements IRepositoryService {
@Autowired
    RepositoryRepository repositoryRepository;

    @Override
    public Repository getRepository() {
        return null;
    }

    @Override
    public void saveRepository(Repository repository) {

    }

    @Override
    public void deleteRepository(Long id) {

    }

    @Override
    public void updateRepository(Long id, Repository repository) {

    }

    @Override
    public Repository getRepositoryById(Long id) {
        return null;
    }

    @Override
    public List<Repository> getAllRepositories() {
        return repositoryRepository.findAll();
    }

    public List<Repository> getRepositoriesByUser(User user) {
        List<Repository> repositories = new ArrayList<>();
        repositories.addAll(repositoryRepository.findByCreator(user));
        repositories.addAll(repositoryRepository.findByCollaboratorsContaining(user));
        return repositories;
    }
}
