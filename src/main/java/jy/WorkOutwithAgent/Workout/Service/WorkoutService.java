package jy.WorkOutwithAgent.Workout.Service;


import jy.WorkOutwithAgent.Workout.DTO.WorkoutDto;
import jy.WorkOutwithAgent.Workout.Repository.WorkoutRepository;
import jy.WorkOutwithAgent.Workout.Entity.Workout;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;


    public List<WorkoutDto> findWorkout(Long memberId, LocalDate workoutDate){
        LocalDateTime startOfDay = workoutDate.atStartOfDay();
        LocalDateTime endOfDay = workoutDate.plusDays(1).atStartOfDay().minusNanos(1);
        List<Workout> workouts = workoutRepository.findByMember_IdAndWorkoutDateBetween(memberId, startOfDay, endOfDay);
        return workouts.stream()
                .map(WorkoutDto::fromEntity)
                .collect(Collectors.toList());
    }



}
