package jy.WorkOutwithAgent.Meal.DTO;


import io.swagger.v3.oas.annotations.media.Schema;

public record NutritionSummaryDto(
        @Schema(description = "총 섭취 칼로리", example ="2000") Double totalCalories,
        @Schema(description = "총 소모 칼로리", example = "600") Double burnedCalories,
        @Schema(description = "총 단백질(g)", example = "30") Double totalProtein,
        @Schema(description = "총 탄수화물(g)", example = "200") Double totalCarbs,
        @Schema(description = "총 지방(g)", example = "20") Double totalFat,

        @Schema(description = "권장 칼로리", example = "1900") Double recommendedCalories,
        @Schema(description = "권장 단백질 비율(%)",example = "15.3") Double recommendedProteinRatio,
        @Schema(description = "권장 탄수화물 비율(%)", example = "25.5") Double recommendedCarbRatio,
        @Schema(description = "권장 지방 비율(%)", example = "10.3") Double recommendedFatRatio
) {}

