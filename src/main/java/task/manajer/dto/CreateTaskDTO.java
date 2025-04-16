package task.manajer.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import task.manajer.domain.TaskStatus;

import java.util.Date;

public record CreateTaskDTO(
        @NotBlank(message = "Name is required")
        @Column(length = 255)
        @Size(min = 3 ,max = 255, message = "Name must be between 3 and 255 characters")
        String name,

        @Column(length = 500)
        @Size(max = 500, message = "Description must not exceed 500 characters")
        @NotBlank(message = "Description is required")
        String description,

        @NotNull
        @Min(value = 1)
        @Max(value = 3)
        Integer priority,

        @Future(message = "Due date must be in the future")
        @NotNull(message = "Due date is required")
        Date dueDate
) {
}
