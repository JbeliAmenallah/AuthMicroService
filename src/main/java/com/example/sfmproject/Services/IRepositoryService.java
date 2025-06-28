package com.example.sfmproject.Services;

import com.example.sfmproject.Entities.Repository;

import java.util.List;

public interface IRepositoryService {

    Repository getRepository();
    void saveRepository(Repository repository);
    void deleteRepository(Long id);
    void updateRepository(Long id, Repository repository);
    Repository getRepositoryById(Long id);
    List<Repository> getAllRepositories();

}
