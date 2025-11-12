package jy.WorkOutwithAgent.Workout.Service;


import jy.WorkOutwithAgent.Auth.Util.AuthUtils;
import jy.WorkOutwithAgent.Member.Entity.Member;
import jy.WorkOutwithAgent.Member.Repository.MemberRepository;
import jy.WorkOutwithAgent.Member.exception.MemberNotFoundException;
import jy.WorkOutwithAgent.Member.Service.CustomUserDetails;
import jy.WorkOutwithAgent.Workout.DTO.WorkoutRequestDto;
import jy.WorkOutwithAgent.Workout.DTO.WorkoutResponseDto;
import jy.WorkOutwithAgent.Workout.Repository.WorkoutRepository;
import jy.WorkOutwithAgent.Workout.Entity.Workout;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final MemberRepository memberRepository;


    public List<WorkoutResponseDto> findWorkout(Long memberId, LocalDate workoutDate){
        LocalDateTime startOfDay = workoutDate.atStartOfDay();
        LocalDateTime endOfDay = workoutDate.plusDays(1).atStartOfDay().minusNanos(1);
        List<Workout> workouts = workoutRepository.findByMember_IdAndWorkoutDateBetween(memberId, startOfDay, endOfDay);
        return workouts.stream()
                .map(WorkoutResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public Workout createWorkout(WorkoutRequestDto workoutRequestDto, CustomUserDetails userDetails) {
        AuthUtils.validateMemberId(workoutRequestDto.getMemberId(), userDetails);

        Member member = memberRepository.findById(workoutRequestDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(workoutRequestDto.getMemberId()));

        Workout workout = Workout.builder()
                .member(member)
                .workoutType(workoutRequestDto.getWorkoutType())
                .exerciseName(workoutRequestDto.getExerciseName())
                .sets(workoutRequestDto.getSets())
                .reps(workoutRequestDto.getReps())
                .weight(workoutRequestDto.getWeight())
                .durationMinutes(workoutRequestDto.getDurationMinutes())
                .distanceKm(workoutRequestDto.getDistanceKm())
                .notes(workoutRequestDto.getNotes())
                .intensity(workoutRequestDto.getIntensity())
                .workoutDate(workoutRequestDto.getWorkoutDate() != null ? workoutRequestDto.getWorkoutDate() : LocalDateTime.now())
                .build();

        return workoutRepository.save(workout);
    }
}
