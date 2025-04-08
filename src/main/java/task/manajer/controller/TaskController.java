package task.manajer.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import task.manajer.domain.Task;
import task.manajer.dto.CreateTaskDTO;
import task.manajer.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping()
    @Transactional
    public ResponseEntity createTask(@RequestBody CreateTaskDTO data, UriComponentsBuilder uriBuilder) {
        Task task = taskService.createTask(data);
        var uri = uriBuilder.path("/tasks/{id}").buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(uri).body(task);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        var tasks = taskService.getAll();
        return ResponseEntity.ok(tasks);
    }
}
