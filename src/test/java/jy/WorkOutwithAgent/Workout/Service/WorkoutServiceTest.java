package jy.WorkOutwithAgent.Workout.Service;

import jy.WorkOutwithAgent.GlobalErrorHandler.GlobalException;
import jy.WorkOutwithAgent.Member.Entity.Member;
import jy.WorkOutwithAgent.Member.Repository.MemberRepository;
import jy.WorkOutwithAgent.Member.Service.CustomUserDetails;
import jy.WorkOutwithAgent.Member.exception.MemberNotFoundException;
import jy.WorkOutwithAgent.Workout.DTO.WorkoutRequestDto;
import jy.WorkOutwithAgent.Workout.DTO.WorkoutResponseDto;
import jy.WorkOutwithAgent.Workout.Entity.Workout;
import jy.WorkOutwithAgent.Workout.Entity.enums.Intensity;
import jy.WorkOutwithAgent.Workout.Entity.enums.WorkoutType;
import jy.WorkOutwithAgent.Workout.Repository.WorkoutRepository;
import jy.WorkOutwithAgent.Workout.Service.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private WorkoutService workoutService;

    private Member member;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .username("testUser")
                .password("password")
                .email("test@test.com")
                .roles(Set.of("ROLE_USER"))
                .build();

        userDetails = new CustomUserDetails(member);
    }

    @Test
    @DisplayName("운동 기록 생성 성공")
    void createWorkout_Success() {
        // given
        WorkoutRequestDto requestDto = WorkoutRequestDto.builder()
                .memberId(1L)
                .exerciseName("스쿼트")
                .workoutType(WorkoutType.STRENGTH_TRAINING)
                .intensity(Intensity.LEVEL_8)
                .reps(10)
                .sets(5)
                .weight(100.0)
                .workoutDate(LocalDateTime.now())
                .build();

        Workout savedWorkout = Workout.builder()
                .id(100L)
                .member(member)
                .exerciseName("스쿼트")
                .workoutType(WorkoutType.STRENGTH_TRAINING)
                .intensity(Intensity.LEVEL_8)
                .reps(10)
                .sets(5)
                .weight(100.0)
                .workoutDate(requestDto.getWorkoutDate())
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(workoutRepository.save(any(Workout.class))).thenReturn(savedWorkout);

        // when
        Workout result = workoutService.createWorkout(requestDto, userDetails);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getExerciseName()).isEqualTo("스쿼트");
        assertThat(result.getMember().getId()).isEqualTo(1L);

        verify(memberRepository).findById(1L);
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    @DisplayName("운동 기록 생성 실패 - 요청한 회원 ID가 DB에 없는 경우")
    void createWorkout_Fails_WhenMemberIdDoesNotExistInDb() {
        // given
        // 로그인한 사용자(ID: 1)가 자신의 운동 기록을 생성하려 함
        WorkoutRequestDto requestDto = WorkoutRequestDto.builder()
                .memberId(1L)
                .exerciseName("스쿼트")
                .build();
        // 따라서 AuthUtils.validateMemberId(1L, userDetails)는 성공적으로 통과함

        // 하지만, DB에서 해당 회원을 찾을 수 없는 상황을 시뮬레이션
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        // 권한 검사는 통과했지만 회원을 찾을 수 없으므로 MemberNotFoundException이 발생해야 함
        assertThrows(MemberNotFoundException.class, () -> {
            workoutService.createWorkout(requestDto, userDetails);
        });

        // findById는 호출되었지만, save는 호출되지 않았는지 확인
        verify(memberRepository).findById(1L);
        verify(workoutRepository, never()).save(any(Workout.class));
    }

    @Test
    @DisplayName("운동 기록 생성 실패 - 권한 없는 사용자")
    void createWorkout_Fails_WhenUserIsUnauthorized() {
        // given
        WorkoutRequestDto requestDto = WorkoutRequestDto.builder()
                .memberId(2L) // Trying to create a workout for another user
                .exerciseName("스쿼트")
                .build();
        // userDetails is for memberId 1L

        // when & then
        assertThrows(GlobalException.class, () -> {
            workoutService.createWorkout(requestDto, userDetails);
        });

        verify(memberRepository, never()).findById(anyLong());
        verify(workoutRepository, never()).save(any(Workout.class));
    }


    @Test
    @DisplayName("특정 날짜 운동 조회 성공시 DTO 리스트 반환 및 날짜 범위 검증")
    void findWorkout_returnsWorkoutDtos() {
        // given
        Long memberId = 5L;
        LocalDate targetDate = LocalDate.of(2025, 1, 15);
        Member findMember = Member.builder()
                .id(memberId)
                .username("sampleUser")
                .password("password")
                .email("sample@example.com")
                .displayName("샘플")
                .build();

        Workout workout = Workout.builder()
                .id(10L)
                .member(findMember)
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
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(workout));

        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        // when
        List<WorkoutResponseDto> result = workoutService.findWorkout(memberId, targetDate);

        // then
        assertThat(result).hasSize(1);
        WorkoutResponseDto dto = result.get(0);
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
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(Collections.emptyList());

        // when
        List<WorkoutResponseDto> result = workoutService.findWorkout(memberId, targetDate);

        // then
        assertThat(result).isEmpty();
    }
}


