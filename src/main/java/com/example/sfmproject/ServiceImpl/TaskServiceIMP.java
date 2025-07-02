package com.example.sfmproject.ServiceImpl;
import com.example.sfmproject.Entities.Phase;
import com.example.sfmproject.Entities.Repository;
import com.example.sfmproject.Entities.Task;
import com.example.sfmproject.Entities.User;
import com.example.sfmproject.JWT.JwtProvider;
import com.example.sfmproject.Repositories.*;
import com.example.sfmproject.Repositories.TaskRepository;
import com.example.sfmproject.Repositories.UserRepository;
import com.example.sfmproject.Services.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 import java.util.List;

 @Service
 public class TaskServiceIMP implements ITaskService {
     @Autowired
     private TaskRepository taskRepository;
     @Autowired
     private PhaseRepository phaseRepository;
     @Autowired
     private UserRepository userRepository;
     @Autowired
     private JwtProvider jwtProvider;
     @Autowired
     private RepositoryRepository repositoryRepository;

     @Override
     public Task createTask(Task task, Long phaseId,Long reposID) {
         Phase phase = phaseRepository.findById(phaseId).orElseThrow(() -> new RuntimeException("Phase not found"));
         Repository repos= repositoryRepository.findById(reposID).get();
         task.setPhase(phase);
         task.setRepository(repos);
         return taskRepository.save(task);
     }

     @Override
     public Task updateTask(Long id, Task task) {
         Task existing = taskRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Task not found"));
         existing.setTitle(task.getTitle());
         existing.setDescription(task.getDescription());
         existing.setGrade(task.getGrade());
         existing.setPriority(task.getPriority());
         existing.setComment(task.getComment());
         existing.setDueDate(task.getDueDate());
         existing.setCompleted(task.isCompleted()); // <-- Make sure this is present!

         // Update assignedTo
         if (task.getAssignedTo() != null && task.getAssignedTo().getId() != null) {
             User user = userRepository.findById(task.getAssignedTo().getId())
                     .orElseThrow(() -> new RuntimeException("User not found"));
             existing.setAssignedTo(user);
         } else {
             existing.setAssignedTo(null);
         }

         // Update phase
         if (task.getPhase() != null && task.getPhase().getId() != null) {
             Phase phase = phaseRepository.findById(task.getPhase().getId())
                     .orElseThrow(() -> new RuntimeException("Phase not found"));
             existing.setPhase(phase);
         } else {
             existing.setPhase(null);
         }

         return taskRepository.save(existing);
     }

     @Override
     public void deleteTask(Long id) {
         taskRepository.deleteById(id);
     }

     @Override
     public Task getTaskById(Long id) {
         return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
     }
     @Override
     public List<Task> getAllTasks() {
         return taskRepository.findAll();
     }

     @Override
     public void assignTaskToUser(Long taskId, Long userId) {
         Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
         User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
         task.setAssignedTo(user);
         taskRepository.save(task);
     }
     @Override
     public void evaluateTask(Long taskId, Double grade) {
         Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
         task.setGrade(grade);
         taskRepository.save(task);
     }
     public void assignTaskToPhase(Long taskId, Long phaseId) {
         Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
         Phase phase = phaseRepository.findById(phaseId).orElseThrow(() -> new RuntimeException("Phase not found"));

         task.setPhase(phase); // Assuming Task has a setPhase method
         taskRepository.save(task);
     }
     @Override
     public List<Task> getTasksByRepository(Long repositoryId) {
         return taskRepository.findByRepositoryId(repositoryId);
     }
 }