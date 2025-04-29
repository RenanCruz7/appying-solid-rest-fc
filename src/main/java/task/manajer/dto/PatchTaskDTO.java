package task.manajer.dto;

import jakarta.validation.constraints.NotNull;
import task.manajer.domain.TaskStatus;

import java.util.Date;

public record PatchTaskDTO(
        String name,
        String description,
        Integer priority,
        TaskStatus status,
        Date dueDate
) {
}
