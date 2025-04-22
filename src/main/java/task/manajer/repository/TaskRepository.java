package task.manajer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import task.manajer.domain.Task;
import task.manajer.domain.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByStatus(TaskStatus status);

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findByPriorityBetween(int min, int max, Pageable pageable);

    Page<Task>findByNameContaining(String name, Pageable pageable);

    @Query("SELECT AVG(t.priority) FROM task t")
    Double getAveragePriority();
}
