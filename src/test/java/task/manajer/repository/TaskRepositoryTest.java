package task.manajer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import task.manajer.domain.Task;
import task.manajer.domain.TaskStatus;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void findByStatus_ShouldReturnTasksWithGivenStatus() {
        // Arrange
        Task todoTask = new Task();
        todoTask.setName("Todo Task");
        todoTask.setStatus(TaskStatus.ToDO);
        todoTask.setPriority(1);
        todoTask.setDueDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Task doingTask = new Task();
        doingTask.setName("Doing Task");
        doingTask.setStatus(TaskStatus.Doing);
        doingTask.setPriority(2);
        doingTask.setDueDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        taskRepository.save(todoTask);
        taskRepository.save(doingTask);

        // Act
        Page<Task> result = taskRepository.findByStatus(TaskStatus.ToDO, PageRequest.of(0, 10));

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Todo Task", result.getContent().get(0).getName());
    }

    @Test
    void findByNameContaining_ShouldReturnTasksWithNameMatching() {
        // Arrange
        Task task1 = new Task();
        task1.setName("Projeto Frontend");
        task1.setStatus(TaskStatus.ToDO);
        task1.setPriority(1);
        task1.setDueDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Task task2 = new Task();
        task2.setName("Projeto Backend");
        task2.setStatus(TaskStatus.Doing);
        task2.setPriority(2);
        task2.setDueDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        taskRepository.save(task1);
        taskRepository.save(task2);

        // Act
        Page<Task> result = taskRepository.findByNameContaining("Frontend", PageRequest.of(0, 10));

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Projeto Frontend", result.getContent().get(0).getName());
    }
}