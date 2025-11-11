package jy.WorkOutwithAgent.Meal.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jy.WorkOutwithAgent.Auth.Util.AuthUtils;
import jy.WorkOutwithAgent.GlobalErrorHandler.GlobalException;
import jy.WorkOutwithAgent.Meal.DTO.NutritionSummaryDto;
import jy.WorkOutwithAgent.Meal.Service.NutritionService;
import jy.WorkOutwithAgent.Member.Service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/meal")
@RequiredArgsConstructor
public class MealController {
    private final NutritionService nutritionService;


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


}

