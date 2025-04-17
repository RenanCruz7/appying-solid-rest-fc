package task.manajer.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.manajer.domain.Task;
import task.manajer.domain.TaskStatus;
import task.manajer.dto.CreateTaskDTO;
import task.manajer.dto.PatchTaskDTO;
import task.manajer.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(CreateTaskDTO data) {
        var task = new Task(data);
        return taskRepository.save(task);
    }

    public Page<Task> getAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }
    public Page<Task> findByStatus(TaskStatus status, Pageable pageable) {
        return taskRepository.findByStatus(status, pageable);
    }

    public Task updateTask(Long id, @Valid PatchTaskDTO data) {
        Task task = getById(id);
        if (data.name() != null) {
            task.setName(data.name());
        }
        if (data.description() != null) {
            task.setDescription(data.description());
        }
        if (data.priority() != null) {
            task.setPriority(data.priority());
        }
        if (data.status() != null) {
            task.setStatus(data.status());
        }
        if (data.dueDate() != null) {
            task.setDueDate(data.dueDate());
        }
        return taskRepository.save(task);
    }
}
