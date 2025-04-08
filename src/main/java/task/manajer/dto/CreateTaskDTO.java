package task.manajer.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import task.manajer.domain.TaskStatus;

import java.util.Date;

public record CreateTaskDTO(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotBlank
        @Min(1)
        @Max(3)
        int priority,
        @NotBlank
        Date dueDate
) {
}
