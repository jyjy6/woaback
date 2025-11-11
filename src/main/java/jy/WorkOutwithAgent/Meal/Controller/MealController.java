package jy.WorkOutwithAgent.Meal.Controller;


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


    @GetMapping
    public ResponseEntity<NutritionSummaryDto> todaysSummary(@RequestParam("id") Long memberId,
                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        AuthUtils.validateMemberId(memberId, userDetails);

        NutritionSummaryDto summary = nutritionService.getTodaySummary(memberId);
        return ResponseEntity.ok(summary);
    }












}

