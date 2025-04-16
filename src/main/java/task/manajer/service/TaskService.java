package task.manajer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.manajer.domain.Task;
import task.manajer.dto.CreateTaskDTO;
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
}
