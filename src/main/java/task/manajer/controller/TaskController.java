package task.manajer.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import task.manajer.domain.Task;
import task.manajer.domain.TaskStatus;
import task.manajer.dto.CreateTaskDTO;
import task.manajer.dto.DetailsTaskDTO;
import task.manajer.dto.PatchTaskDTO;
import task.manajer.service.TaskService;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping()
    @Transactional
    public ResponseEntity createTask(@RequestBody @Valid CreateTaskDTO data, UriComponentsBuilder uriBuilder) {
        Task task = taskService.createTask(data);
        var uri = uriBuilder.path("/tasks/{id}").buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetailsTaskDTO(task));
    }

    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(@PageableDefault(size = 10,sort = {"dueDate"}) Pageable pageable) {
        var tasks = taskService.getAll(pageable);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<Task>> getTasksByStatus(
            @PathVariable TaskStatus status,
            @PageableDefault(size = 10, sort = {"dueDate"}) Pageable pageable) {
        var tasks = taskService.findByStatus(status, pageable);
        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DetailsTaskDTO> getTaskById(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return ResponseEntity.ok(new DetailsTaskDTO(task));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DetailsTaskDTO> updateTask(@PathVariable Long id, @RequestBody @Valid PatchTaskDTO data) {
        Task task = taskService.updateTask(id, data);
        return ResponseEntity.ok(new DetailsTaskDTO(task));
    }

}
