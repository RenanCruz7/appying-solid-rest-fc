package task.manajer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import task.manajer.domain.Task;
import task.manajer.domain.TaskStatus;
import task.manajer.dto.TaskStatsDTO;
import task.manajer.repository.TaskRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
}