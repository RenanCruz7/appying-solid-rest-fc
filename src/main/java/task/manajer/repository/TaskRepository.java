package task.manajer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.manajer.domain.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
