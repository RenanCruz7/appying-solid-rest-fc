package task.manajer.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import task.manajer.domain.TaskStatus;

import java.util.Date;

public record CreateTaskDTO(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotNull
        @Min(value = 1)
        @Max(value = 3)
        Integer priority,
        @NotNull
        Date dueDate
) {
}
