package task.manajer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import task.manajer.domain.Task;
import task.manajer.domain.TaskStatus;
import task.manajer.dto.CreateTaskDTO;
import task.manajer.dto.PatchTaskDTO;
import task.manajer.dto.TaskStatsDTO;
import task.manajer.repository.TaskRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTaskStats_ShouldReturnCorrectStats() {
        // Arrange
        when(taskRepository.countByStatus(TaskStatus.ToDO)).thenReturn(5L);
        when(taskRepository.countByStatus(TaskStatus.Doing)).thenReturn(3L);
        when(taskRepository.countByStatus(TaskStatus.Done)).thenReturn(2L);
        when(taskRepository.getAveragePriority()).thenReturn(2.5);

        // Act
        TaskStatsDTO result = taskService.getTaskStats();

        // Assert
        assertEquals(5L, result.todoCount());
        assertEquals(3L, result.doingCount());
        assertEquals(2L, result.doneCount());
        assertEquals(2.5, result.averagePriority());
    }

    @Test
    void getById_WhenTaskExists_ShouldReturnTask() {
        // Arrange
        Task expectedTask = new Task();
        expectedTask.setId(1L);
        expectedTask.setName("Test Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(expectedTask));

        // Act
        Task result = taskService.getById(1L);

        // Assert
        assertEquals(expectedTask.getId(), result.getId());
        assertEquals(expectedTask.getName(), result.getName());
    }

    @Test
    void getById_WhenTaskDoesNotExist_ShouldThrowException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taskService.getById(999L));
    }

    @Test
    void createTask_ShouldCreateAndReturnTask() {
        // Arrange
        CreateTaskDTO dto = new CreateTaskDTO(
                "Nova Tarefa",
                "Descrição",
                3,
                Date.from(LocalDate.of(2025, 5, 30).atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
        Task savedTask = new Task(dto);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        Task result = taskService.createTask(dto);

        // Assert
        assertEquals("Nova Tarefa", result.getName());
        assertEquals("Descrição", result.getDescription());
        assertEquals(3, result.getPriority());
        assertEquals(TaskStatus.ToDO, result.getStatus());
    }

    @Test
    void getAll_ShouldReturnPageOfTasks() {
        // Arrange
        Task task1 = new Task();
        task1.setName("Tarefa 1");
        Task task2 = new Task();
        task2.setName("Tarefa 2");
        List<Task> tasks = List.of(task1, task2);

        Page<Task> taskPage = new PageImpl<>(tasks);
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        // Act
        Page<Task> result = taskService.getAll(pageable);

        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals("Tarefa 1", result.getContent().get(0).getName());
        assertEquals("Tarefa 2", result.getContent().get(1).getName());
    }

    @Test
    void findByStatus_ShouldReturnTasksWithSpecifiedStatus() {
        // Arrange
        Task task = new Task();
        task.setName("Tarefa em andamento");
        task.setStatus(TaskStatus.Doing);

        Page<Task> taskPage = new PageImpl<>(List.of(task));
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepository.findByStatus(TaskStatus.Doing, pageable)).thenReturn(taskPage);

        // Act
        Page<Task> result = taskService.findByStatus(TaskStatus.Doing, pageable);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals(TaskStatus.Doing, result.getContent().get(0).getStatus());
    }

    @Test
    void updateTask_WithCompleteData_ShouldUpdateAllFields() {
        // Arrange
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setName("Original");
        existingTask.setDescription("Descrição original");
        existingTask.setPriority(1);
        existingTask.setStatus(TaskStatus.ToDO);

        PatchTaskDTO dto = new PatchTaskDTO("Atualizada", "Nova descrição", 3, TaskStatus.Doing, null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Task result = taskService.updateTask(1L, dto);

        // Assert
        assertEquals("Atualizada", result.getName());
        assertEquals("Nova descrição", result.getDescription());
        assertEquals(3, result.getPriority());
        assertEquals(TaskStatus.Doing, result.getStatus());
    }

    @Test
    void updateTask_WithPartialData_ShouldUpdateOnlyProvidedFields() {
        // Arrange
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setName("Original");
        existingTask.setDescription("Descrição original");
        existingTask.setPriority(1);
        existingTask.setStatus(TaskStatus.ToDO);

        PatchTaskDTO dto = new PatchTaskDTO("Atualizada", null, null, null, null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Task result = taskService.updateTask(1L, dto);

        // Assert
        assertEquals("Atualizada", result.getName());
        assertEquals("Descrição original", result.getDescription());
        assertEquals(1, result.getPriority());
        assertEquals(TaskStatus.ToDO, result.getStatus());
    }

    @Test
    void findByPriorityBetween_ShouldReturnTasksWithPriorityInRange() {
        // Arrange
        Task task1 = new Task();
        task1.setPriority(2);
        Task task2 = new Task();
        task2.setPriority(3);

        Page<Task> taskPage = new PageImpl<>(List.of(task1, task2));
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepository.findByPriorityBetween(1, 3, pageable)).thenReturn(taskPage);

        // Act
        Page<Task> result = taskService.findByPriorityBetween(1, 3, pageable);

        // Assert
        assertEquals(2, result.getContent().size());
    }

    @Test
    void findByNameContaining_ShouldReturnTasksWithMatchingName() {
        // Arrange
        Task task = new Task();
        task.setName("Projeto Java");

        Page<Task> taskPage = new PageImpl<>(List.of(task));
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepository.findByNameContaining("Java", pageable)).thenReturn(taskPage);

        // Act
        Page<Task> result = taskService.findByNameContaining("Java", pageable);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals("Projeto Java", result.getContent().get(0).getName());
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldDeleteTask() {
        // Arrange
        Task task = new Task();
        task.setId(1L);
        task.setName("Tarefa para excluir");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void deleteTask_WhenTaskDoesNotExist_ShouldThrowException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taskService.deleteTask(999L));
        verify(taskRepository, never()).delete(any(Task.class));
    }

}