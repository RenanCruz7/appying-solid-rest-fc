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

    @Test
    void countByStatus_ShouldReturnCorrectCount() {
        // Arrange
        Task todoTask1 = new Task();
        todoTask1.setName("Todo Task 1");
        todoTask1.setStatus(TaskStatus.ToDO);
        todoTask1.setPriority(1);

        Task todoTask2 = new Task();
        todoTask2.setName("Todo Task 2");
        todoTask2.setStatus(TaskStatus.ToDO);
        todoTask2.setPriority(2);

        Task doingTask = new Task();
        doingTask.setName("Doing Task");
        doingTask.setStatus(TaskStatus.Doing);
        doingTask.setPriority(3);

        taskRepository.save(todoTask1);
        taskRepository.save(todoTask2);
        taskRepository.save(doingTask);

        // Act
        long todoCount = taskRepository.countByStatus(TaskStatus.ToDO);
        long doingCount = taskRepository.countByStatus(TaskStatus.Doing);

        // Assert
        assertEquals(2, todoCount);
        assertEquals(1, doingCount);
    }

    @Test
    void findByPriorityBetween_ShouldReturnTasksWithPriorityInRange() {
        // Arrange
        Task lowPriorityTask = new Task();
        lowPriorityTask.setName("Baixa Prioridade");
        lowPriorityTask.setStatus(TaskStatus.ToDO);
        lowPriorityTask.setPriority(1);
        lowPriorityTask.setDueDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Task mediumPriorityTask = new Task();
        mediumPriorityTask.setName("Média Prioridade");
        mediumPriorityTask.setStatus(TaskStatus.ToDO);
        mediumPriorityTask.setPriority(3);
        mediumPriorityTask.setDueDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Task highPriorityTask = new Task();
        highPriorityTask.setName("Alta Prioridade");
        highPriorityTask.setStatus(TaskStatus.ToDO);
        highPriorityTask.setPriority(3);
        highPriorityTask.setDueDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        taskRepository.save(lowPriorityTask);
        taskRepository.save(mediumPriorityTask);
        taskRepository.save(highPriorityTask);

        // Act
        Page<Task> result = taskRepository.findByPriorityBetween(1, 3, PageRequest.of(0, 10));

        // Assert
        assertEquals(3, result.getTotalElements());
    }

    @Test
    void getAveragePriority_ShouldReturnCorrectAverage() {
        // Arrange
        Task task1 = new Task();
        task1.setName("Task 1");
        task1.setStatus(TaskStatus.ToDO);
        task1.setPriority(2);

        Task task2 = new Task();
        task2.setName("Task 2");
        task2.setStatus(TaskStatus.Doing);
        task2.setPriority(3);

        Task task3 = new Task();
        task3.setName("Task 3");
        task3.setStatus(TaskStatus.Done);
        task3.setPriority(3);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // Act
        Double avgPriority = taskRepository.getAveragePriority();

        // Assert
        assertEquals(2.667, avgPriority, 0.001);
    }

    @Test
    void findAll_ShouldReturnAllTasks() {
        // Arrange
        taskRepository.deleteAll(); // Limpa o repositório para garantir contagem exata

        Task task1 = new Task();
        task1.setName("Task 1");
        task1.setStatus(TaskStatus.ToDO);
        task1.setPriority(1); // Adicionando prioridade mínima
        task1.setDueDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())); // Adicionando data

        Task task2 = new Task();
        task2.setName("Task 2");
        task2.setStatus(TaskStatus.Doing);
        task2.setPriority(2); // Adicionando prioridade
        task2.setDueDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())); // Adicionando data

        taskRepository.save(task1);
        taskRepository.save(task2);

        // Act
        Page<Task> result = taskRepository.findAll(PageRequest.of(0, 10));

        // Assert
        assertEquals(2, result.getTotalElements());
    }
}