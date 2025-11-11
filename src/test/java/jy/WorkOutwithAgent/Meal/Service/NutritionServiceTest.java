package jy.WorkOutwithAgent.Meal.Service;

import jy.WorkOutwithAgent.GlobalErrorHandler.GlobalException;
import jy.WorkOutwithAgent.Meal.DTO.NutritionSummaryDto;
import jy.WorkOutwithAgent.Meal.Entity.Meal;
import jy.WorkOutwithAgent.Meal.Repository.MealRepository;
import jy.WorkOutwithAgent.Member.Entity.Member;
import jy.WorkOutwithAgent.Member.Repository.MemberRepository;
import jy.WorkOutwithAgent.Workout.Entity.Workout;
import jy.WorkOutwithAgent.Workout.Repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NutritionServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private NutritionService nutritionService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .weight(70.0)
                .height(175.0)
                .age(30)
                .sex("male")
                .build();
    }

    @Test
    @DisplayName("오늘의 영양 정보 요약 - 성공")
    void getTodaySummary_Success() {
        // given
        Meal meal = Meal.builder()
            .calories(100.0).protein(10.0).carbohydrates(10.0).fat(10.0)
            .build();
        Workout workout = Workout.builder()
            .workoutType(jy.WorkOutwithAgent.Workout.Entity.enums.WorkoutType.CARDIO)
            .intensity(jy.WorkOutwithAgent.Workout.Entity.enums.Intensity.LEVEL_5)
            .durationMinutes(30)
            .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(mealRepository.findTodayMeals(1L)).thenReturn(List.of(meal));
        when(workoutRepository.findTodayWorkouts(1L)).thenReturn(List.of(workout));

        // when
        NutritionSummaryDto summary = nutritionService.getTodaySummary(1L);

        // then
        assertThat(summary).isNotNull();
    }

    @Test
    @DisplayName("오늘의 영양 정보 요약 - 사용자 없음")
    void getTodaySummary_MemberNotFound() {
        // given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () -> {
            nutritionService.getTodaySummary(1L);
        });

        assertThat(exception.getErrorCode()).isEqualTo("MEMBER_NOT_FOUND");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("오늘의 영양 정보 요약 - 식사 및 운동 기록 없음")
    void getTodaySummary_NoMealsAndWorkouts() {
        // given
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(mealRepository.findTodayMeals(1L)).thenReturn(Collections.emptyList());
        when(workoutRepository.findTodayWorkouts(1L)).thenReturn(Collections.emptyList());

        // when
        NutritionSummaryDto summary = nutritionService.getTodaySummary(1L);

        // then
        assertThat(summary).isNotNull();
        assertThat(summary.totalCalories()).isZero();
        assertThat(summary.burnedCalories()).isZero();
        assertThat(summary.totalProtein()).isZero();
        assertThat(summary.totalCarbs()).isZero();
        assertThat(summary.totalFat()).isZero();
    }
}
