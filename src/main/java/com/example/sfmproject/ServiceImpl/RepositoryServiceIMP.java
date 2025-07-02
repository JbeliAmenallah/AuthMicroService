package com.example.sfmproject.ServiceImpl;

import com.example.sfmproject.Entities.Phase;
import com.example.sfmproject.Entities.Repository;
import com.example.sfmproject.Entities.Task;
import com.example.sfmproject.Entities.User;
import com.example.sfmproject.Repositories.RepositoryRepository;
import com.example.sfmproject.Services.IRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
        return repositoryRepository.findById(id).get();
    }

    @Override
    public List<Repository> getAllRepositories() {
        return repositoryRepository.findAll();
    }

    @Override
    public List<Repository> getRepositoriesByUser(User user) {
        List<Repository> repositories = new ArrayList<>();
        repositories.addAll(repositoryRepository.findByCreator(user));
        repositories.addAll(repositoryRepository.findByCollaboratorsContaining(user));
        return repositories;
    }

//    @Override
//    public void assignAverageGradeToRepository(Long repositoryId) {
//        Repository repository = repositoryRepository.findById(repositoryId).orElseThrow(() -> new RuntimeException("Repository not found"));
//
//        double totalGrade = 0.0;
//        int count = 0;
//
//        for (Phase phase : repository.getPhases()) {
//            if (phase.getGrade() != null) { // Ensure grade is not null
//                totalGrade += phase.getGrade();
//                count++;
//            }
//        }
//
//        // Calculate and assign the average grade to the repository
//        if (count > 0) {
//            double averageGrade = totalGrade / count;
//            repository.setGrade((long) averageGrade); // Assuming grade is of type Long
//        } else {
//            repository.setGrade(0L); // Set to 0 if no valid grades
//        }
//
//        repositoryRepository.save(repository); // Persist the updated repository
//    }

    @Override
    public void assignAverageGradeToRepository(Long repositoryId) {
        Repository repository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        double totalGrade = 0.0;
        double totalCoefficient = 0.0;

        for (Phase phase : repository.getPhases()) {
            if (phase.getGrade() != null) { // Ensure grade is not null
                // Check if any task in the phase is completed
                boolean hasCompletedTasks = phase.getTasks().stream()
                        .anyMatch(Task::isCompleted); // Assuming you have an isCompleted method

                // Assign coefficient based on completed tasks
                double coefficient = hasCompletedTasks ? 3.0 : 1.0;

                // Update total grade and total coefficient
                totalGrade += phase.getGrade() * coefficient;
                totalCoefficient += coefficient;
            }
        }

        // Calculate and assign the average grade to the repository
        if (totalCoefficient > 0) {
            double averageGrade = totalGrade / totalCoefficient;
            repository.setGrade((long) averageGrade); // Assuming grade is of type Long
        } else {
            repository.setGrade(0L); // Set to 0 if no valid grades
        }

        repositoryRepository.save(repository); // Persist the updated repository
    }

}
