package jy.WorkOutwithAgent.AI.Tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jy.WorkOutwithAgent.Workout.DTO.WorkoutResponseDto;
import jy.WorkOutwithAgent.Workout.Entity.Workout;
import jy.WorkOutwithAgent.Workout.Repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class WorkoutAndMealTools {
    private final WorkoutRepository workoutRepository;

    /**
     * 사용자 이름(username)으로 회원 정보를 조회합니다.
     *
     * @param username 조회할 회원의 로그인 아이디
     * @param startOfDay 조회할 시작 날짜
     * @param endOfDay 조회할 끝 날짜
     * @return 운동 정보 (운동이 존재하는 경우), null (존재하지 않는 경우)
     */
    @Tool("사용자 이름(로그인 아이디)으로 회원의 운동 정보를 조회합니다. startOfDay는 시작날짜, endOfDay는 끝날짜로 날짜를 지정할 수 있습니다. 운동정보가 존재하면 정보를 반환하고, 존재하지 않으면 null을 반환합니다.")
    public List<WorkoutResponseDto> findWorkoutsByUsername(
            @P("(username), (startOfDay), (endOfDay) 로 조회할 회원의 Workout목록. 운동목록.") String username, LocalDateTime startOfDay, LocalDateTime endOfDay
    ) {
        log.info("툴 호출: findWorkoutsByUsername - username: {}", username);

        List<Workout> workouts = workoutRepository.findByMember_UsernameAndWorkoutDateBetween(username, startOfDay, endOfDay);
        return workouts.stream()
                .map(WorkoutResponseDto::fromEntity)
                .collect(Collectors.toList());
    }




}
