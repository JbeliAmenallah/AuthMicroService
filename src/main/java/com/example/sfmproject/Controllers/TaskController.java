package com.example.sfmproject.Controllers;


import com.example.sfmproject.Entities.Task;
import com.example.sfmproject.ServiceImpl.PhaseServiceIMP;
import com.example.sfmproject.Services.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private ITaskService taskService;
    @Autowired
    private PhaseServiceIMP phaseServiceIMP;

    @PostMapping("/{phaseId}")
    public ResponseEntity<Task> createTask(@RequestBody Task task, @PathVariable Long phaseId) {
        return ResponseEntity.ok(taskService.createTask(task, phaseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<Void> assignTaskToUser(@PathVariable Long taskId, @PathVariable Long userId) {
        taskService.assignTaskToUser(taskId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{taskId}/evaluate")
    @PreAuthorize("hasRole('enseignant')")
    public ResponseEntity<Void> evaluateTask(@PathVariable Long taskId, @RequestParam Double grade) {
        taskService.evaluateTask(taskId, grade);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{taskId}/assign-phase/{phaseId}")
    public ResponseEntity<Void> assignTaskToPhase(@PathVariable Long taskId, @PathVariable Long phaseId) {
        taskService.assignTaskToPhase(taskId, phaseId);
        return ResponseEntity.ok().build(); // Return 200 OK if successful
    }

}
