package jy.WorkOutwithAgent.Workout.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jy.WorkOutwithAgent.Auth.Util.AuthUtils;
import jy.WorkOutwithAgent.Member.Service.CustomUserDetails;
import jy.WorkOutwithAgent.Workout.DTO.WorkoutRequestDto;
import jy.WorkOutwithAgent.Workout.DTO.WorkoutResponseDto;
import jy.WorkOutwithAgent.Workout.Service.WorkoutService;
import jy.WorkOutwithAgent.Workout.Entity.Workout;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/workout")
@Tag(name = "운동", description = "운동 기록 관련 API")
public class WorkoutController {
    private final WorkoutService workoutService;

    @Operation(
            summary = "특정 날짜 운동 목록 조회",
            description = "요청한 날짜에 해당 사용자가 기록한 운동 목록을 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "운동 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청(로그인 필요)")
    })
    @GetMapping
    public ResponseEntity<List<WorkoutResponseDto>> getWorkout(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate requestDate
    ){
        AuthUtils.loginCheck(customUserDetails);
        List<WorkoutResponseDto> workouts = workoutService.findWorkout(customUserDetails.getId(), requestDate);
        return ResponseEntity.ok(workouts);
    }

    @Operation(
            summary = "운동 기록 생성",
            description = "새로운 운동 기록을 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "운동 기록 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청(로그인 필요)")
    })
    @PostMapping
    public ResponseEntity<WorkoutResponseDto> createWorkout(
            @RequestBody WorkoutRequestDto workoutRequestDto,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        AuthUtils.loginCheck(customUserDetails);
        Workout createdWorkout = workoutService.createWorkout(workoutRequestDto, customUserDetails);
        WorkoutResponseDto createdWorkoutDto = WorkoutResponseDto.fromEntity(createdWorkout);

        // Return 201 Created with the location of the new resource
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkoutDto);
    }

}
