package jy.WorkOutwithAgent.Meal.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jy.WorkOutwithAgent.Auth.Util.AuthUtils;
import jy.WorkOutwithAgent.Meal.DTO.MealRequestDto;
import jy.WorkOutwithAgent.Meal.DTO.MealResponseDto;
import jy.WorkOutwithAgent.Meal.DTO.NutritionSummaryDto;
import jy.WorkOutwithAgent.Meal.Entity.Meal;
import jy.WorkOutwithAgent.Meal.Service.MealService;
import jy.WorkOutwithAgent.Meal.Service.NutritionService;
import jy.WorkOutwithAgent.Member.Service.CustomUserDetails;
import jy.WorkOutwithAgent.Workout.DTO.WorkoutResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/meal")
@RequiredArgsConstructor
public class MealController {
    private final NutritionService nutritionService;
    private final MealService mealService;


    @Operation(summary = "오늘의 영양 정보 요약", description = "사용자의 ID를 기반으로 오늘 섭취한 총 칼로리, 소모 칼로리 및 주요 영양소(단백질, 탄수화물, 지방)와 권장 섭취량을 요약하여 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "영양 정보 요약 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NutritionSummaryDto.class))}),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    @GetMapping
    public ResponseEntity<NutritionSummaryDto> todaysSummary(@RequestParam("id") Long memberId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        AuthUtils.validateMemberId(memberId, userDetails);

        NutritionSummaryDto summary = nutritionService.getTodaySummary(memberId);
        return ResponseEntity.ok(summary);
    }


    @Operation(summary = "식사 기록 생성", description = "새로운 식사 기록을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "식사 기록 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청(로그인 필요)")
    })
    @PostMapping
    public ResponseEntity<MealResponseDto> createMeal(
            @RequestBody MealRequestDto mealRequestDto,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AuthUtils.loginCheck(userDetails);
        Meal createdMeal = mealService.createMeal(mealRequestDto, userDetails);
        MealResponseDto responseDto = MealResponseDto.fromEntity(createdMeal);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<MealResponseDto>> getRecentMeals(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "조회할 최근 식사 개수", example = "1")
            @RequestParam(value = "limit", defaultValue = "1")
            int limit
    ) {

        List<MealResponseDto> recentMeals = mealService.recentMeals(customUserDetails.getId(), limit);
        return ResponseEntity.ok(recentMeals);
    }



}

