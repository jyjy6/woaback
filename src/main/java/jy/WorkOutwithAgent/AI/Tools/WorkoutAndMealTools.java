package jy.WorkOutwithAgent.AI.Tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jy.WorkOutwithAgent.Meal.DTO.MealResponseDto;
import jy.WorkOutwithAgent.Meal.Entity.Meal;
import jy.WorkOutwithAgent.Meal.Repository.MealRepository;
import jy.WorkOutwithAgent.Workout.DTO.WorkoutResponseDto;
import jy.WorkOutwithAgent.Workout.Entity.Workout;
import jy.WorkOutwithAgent.Workout.Repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class WorkoutAndMealTools {
    private final WorkoutRepository workoutRepository;
    private final MealRepository mealRepository;

    /**
     * 사용자 이름(username)으로 회원 정보를 조회합니다.
     *
     * @param username 조회할 회원의 로그인 아이디
     * @param startOfDay 조회할 시작 날짜
     * @param endOfDay 조회할 끝 날짜
     * @return 운동 정보 (운동이 존재하는 경우), null (존재하지 않는 경우)
     */

    @Tool("사용자 이름(로그인 아이디)으로 회원의 운동 정보를 조회합니다. username은 회원의 아이디, startOfDay는 시작날짜, endOfDay는 끝날짜로 날짜를 지정할 수 있습니다. 운동정보가 존재하면 정보를 반환하고, 존재하지 않으면 null을 반환합니다.")
    public List<WorkoutResponseDto> findWorkoutsByUsernameForAI(
            @P("조회할 회원의 로그인 아이디 (username)") String username,@P("시작날짜. startOfDay") LocalDateTime startOfDay, @P("끝 날짜. endOfDay")LocalDateTime endOfDay
    ) {
        log.info("툴 호출: findWorkoutsByUsernameForAI - username: {}", username);

        List<Workout> workouts = workoutRepository.findByMember_UsernameAndWorkoutDateBetween(username, startOfDay, endOfDay);
        return workouts.stream()
                .map(WorkoutResponseDto::fromEntity)
                .collect(Collectors.toList());
    }


    @Tool("사용자 식별자로 회원의 최근 운동정보를 조회합니다. memberId는 회원의 고유식별자(id), limit은 최근 N개의 운동정보를 가져올건지의 개수")
    public List<WorkoutResponseDto> getRecentWorkoutsForAI(@P("조회할 회원의 식별자(id)") Long memberId,
                                                           @P("최근 N개의 운동 개수(limit)") int limit){
        Pageable pageable = PageRequest.of(0, limit);
        List<Workout> workouts = workoutRepository.findByMemberIdOrderByWorkoutDateDesc(memberId, pageable);

        return workouts.stream()
                .map(WorkoutResponseDto::fromEntity)
                .toList();
    }


    @Tool("사용자 식별자로 회원의 최근 식사정보를 조회합니다. memberId는 회원의 고유식별자(id), limit은 최근 N개의 식사정보를 가져올건지의 개수")
    public List<MealResponseDto> recentMeals(@P("조회할 회원의 식별자(id)") Long memberId,
                                             @P("최근 N개의 식사 개수(limit)") int limit){
        Pageable pageable = PageRequest.of(0, limit);
        List<Meal> workouts = mealRepository.findByMemberIdOrderByMealDateDesc(memberId, pageable);

        return workouts.stream()
                .map(MealResponseDto::fromEntity)
                .toList();
    }







}
