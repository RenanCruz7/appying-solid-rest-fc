package task.manajer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.manajer.domain.Task;
import task.manajer.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }
}
