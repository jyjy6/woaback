package jy.WorkOutwithAgent.Meal.DTO;


public record NutritionSummaryDto(
        double totalCalories,
        double burnedCalories,
        double totalProtein,
        double totalCarbs,
        double totalFat,
        double recommendedCalories,
        double recommendedProteinRatio,
        double recommendedCarbRatio,
        double recommendedFatRatio
) {
}
