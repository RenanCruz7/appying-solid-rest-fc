package task.manajer.dto;

import task.manajer.domain.Task;
import task.manajer.domain.TaskStatus;

import java.util.Date;

public record DetailsTaskDTO(Long id, String name, String description, TaskStatus status, Date dueDate) {

    public DetailsTaskDTO(Task task){
        this(task.getId(),
                task.getName(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate());
    }

}
