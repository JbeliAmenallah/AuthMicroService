package com.example.sfmproject.Repositories;

import com.example.sfmproject.Entities.Classe;
import com.example.sfmproject.Entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
