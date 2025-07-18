package com.example.sfmproject.Repositories;

import com.example.sfmproject.Entities.Classe;
import com.example.sfmproject.Entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByRepositoryId(Long repositoryId); // Method to find tasks by repository ID

}
