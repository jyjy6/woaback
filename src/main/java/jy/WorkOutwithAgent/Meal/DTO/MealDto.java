package jy.WorkOutwithAgent.Meal.DTO;

import jy.WorkOutwithAgent.Meal.Entity.Meal;
import jy.WorkOutwithAgent.Meal.Entity.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MealDto {
    private Long id;
    private Long memberId;
    private MealType mealType;
    private String foodName;
    private Double calories;
    private Double protein;
    private Double carbohydrates;
    private Double fat;
    private LocalDateTime mealDate;
    private String notes;
    private String imageUrl;

    public static MealDto fromEntity(Meal meal) {
        return MealDto.builder()
                .id(meal.getId())
                .memberId(meal.getMember().getId())
                .mealType(meal.getMealType())
                .foodName(meal.getFoodName())
                .calories(meal.getCalories())
                .protein(meal.getProtein())
                .carbohydrates(meal.getCarbohydrates())
                .fat(meal.getFat())
                .mealDate(meal.getMealDate())
                .notes(meal.getNotes())
                .imageUrl(meal.getImageUrl())
                .build();
    }
}