package com.example.sfmproject.ServiceImpl;
import com.example.sfmproject.Entities.Phase;
import com.example.sfmproject.Entities.Task;
import com.example.sfmproject.Entities.User;
import com.example.sfmproject.JWT.JwtProvider;
import com.example.sfmproject.Repositories.PhaseRepository;
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

     @Override
     public Task createTask(Task task, Long phaseId) {
         Phase phase = phaseRepository.findById(phaseId).orElseThrow(() -> new RuntimeException("Phase not found"));
         task.setPhase(phase);
         return taskRepository.save(task);
     }

     @Override
     public Task updateTask(Long id, Task task) {
         Task existing = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
         existing.setTitle(task.getTitle());
         existing.setDescription(task.getDescription());
         existing.setGrade(task.getGrade());
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
 }