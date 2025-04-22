package task.manajer.dto;

public record TaskStatsDTO(
        long todoCount,
        long doingCount,
        long doneCount,
        double averagePriority
) {
}
