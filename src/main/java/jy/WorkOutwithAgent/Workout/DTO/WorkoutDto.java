package jy.WorkOutwithAgent.Workout.DTO;

import jy.WorkOutwithAgent.Workout.Entity.Workout;
import jy.WorkOutwithAgent.Workout.Entity.enums.WorkoutType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkoutDto {
    private Long id;
    private Long memberId;
    private WorkoutType workoutType;
    private String exerciseName;
    private Integer sets;
    private Integer reps;
    private Double weight;
    private Integer durationMinutes;
    private Double distanceKm;
    private LocalDateTime workoutDate;
    private String notes;

    public static WorkoutDto fromEntity(Workout workout) {
        return WorkoutDto.builder()
                .id(workout.getId())
                .memberId(workout.getMember().getId()) // Avoid circular reference
                .workoutType(workout.getWorkoutType())
                .exerciseName(workout.getExerciseName())
                .sets(workout.getSets())
                .reps(workout.getReps())
                .weight(workout.getWeight())
                .durationMinutes(workout.getDurationMinutes())
                .distanceKm(workout.getDistanceKm())
                .workoutDate(workout.getWorkoutDate())
                .notes(workout.getNotes())
                .build();
    }
}