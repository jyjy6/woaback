package jy.WorkOutwithAgent.Workout.Service;

import jy.WorkOutwithAgent.Member.Entity.Member;
import jy.WorkOutwithAgent.Workout.DTO.WorkoutDto;
import jy.WorkOutwithAgent.Workout.Entity.Workout;
import jy.WorkOutwithAgent.Workout.Entity.enums.Intensity;
import jy.WorkOutwithAgent.Workout.Entity.enums.WorkoutType;
import jy.WorkOutwithAgent.Workout.Repository.WorkoutRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutService workoutService;

    @Test
    @DisplayName("특정 날짜 운동 조회 성공시 DTO 리스트 반환 및 날짜 범위 검증")
    void findWorkout_returnsWorkoutDtos() {
        // given
        Long memberId = 5L;
        LocalDate targetDate = LocalDate.of(2025, 1, 15);
        Member member = Member.builder()
                .id(memberId)
                .username("sampleUser")
                .password("password")
                .email("sample@example.com")
                .displayName("샘플")
                .build();

        Workout workout = Workout.builder()
                .id(10L)
                .member(member)
                .workoutType(WorkoutType.CARDIO)
                .exerciseName("런닝")
                .sets(3)
                .reps(12)
                .weight(40.0)
                .durationMinutes(45)
                .distanceKm(5.5)
                .intensity(Intensity.LEVEL_3)
                .workoutDate(targetDate.atTime(9, 30))
                .notes("Morning run")
                .build();

        when(workoutRepository.findByMember_IdAndWorkoutDateBetween(
                eq(memberId),
                org.mockito.ArgumentMatchers.any(LocalDateTime.class),
                org.mockito.ArgumentMatchers.any(LocalDateTime.class)
        )).thenReturn(List.of(workout));

        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        // when
        List<WorkoutDto> result = workoutService.findWorkout(memberId, targetDate);

        // then
        assertThat(result).hasSize(1);
        WorkoutDto dto = result.get(0);
        assertThat(dto.getId()).isEqualTo(workout.getId());
        assertThat(dto.getMemberId()).isEqualTo(memberId);
        assertThat(dto.getExerciseName()).isEqualTo("런닝");
        assertThat(dto.getWorkoutDate()).isEqualTo(workout.getWorkoutDate());

        verify(workoutRepository).findByMember_IdAndWorkoutDateBetween(eq(memberId), startCaptor.capture(), endCaptor.capture());

        LocalDateTime expectedStart = targetDate.atStartOfDay();
        LocalDateTime expectedEnd = targetDate.plusDays(1).atStartOfDay().minusNanos(1);
        assertThat(startCaptor.getValue()).isEqualTo(expectedStart);
        assertThat(endCaptor.getValue()).isEqualTo(expectedEnd);
    }

    @Test
    @DisplayName("특정 날짜 운동 기록이 없으면 빈 리스트 반환")
    void findWorkout_returnsEmptyListWhenNoWorkouts() {
        // given
        Long memberId = 7L;
        LocalDate targetDate = LocalDate.of(2025, 1, 1);
        when(workoutRepository.findByMember_IdAndWorkoutDateBetween(
                eq(memberId),
                org.mockito.ArgumentMatchers.any(LocalDateTime.class),
                org.mockito.ArgumentMatchers.any(LocalDateTime.class)
        )).thenReturn(Collections.emptyList());

        // when
        List<WorkoutDto> result = workoutService.findWorkout(memberId, targetDate);

        // then
        assertThat(result).isEmpty();
    }
}


