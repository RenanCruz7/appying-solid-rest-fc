package task.manajer.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import task.manajer.dto.CreateTaskDTO;

import java.util.Date;

@Table(name = "tasks")
@Entity(name = "task")
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.ToDO;
    @Min(1)
    @Max(3)
    private int priority;
    private Date dueDate;

    public Task(CreateTaskDTO data) {
        this.name = data.name();
        this.description = data.description();
        this.priority = data.priority();
        this.dueDate = data.dueDate();
    }
}
