package com.example.sfmproject.Services;

import com.example.sfmproject.Entities.Task;
import java.util.List;

public interface ITaskService {
    public Task createTask(Task task, Long phaseId,Long reposID);
    Task updateTask(Long id, Task task);
    void deleteTask(Long id);
    Task getTaskById(Long id);
    List<Task> getAllTasks();
    void assignTaskToUser(Long taskId, Long userId);
    void evaluateTask(Long taskId, Double grade);
    public void assignTaskToPhase(Long taskId, Long phaseId);
    public List<Task> getTasksByRepository(Long repositoryId);
//    Double getAverageGradeForPhase(Long phaseId);
}