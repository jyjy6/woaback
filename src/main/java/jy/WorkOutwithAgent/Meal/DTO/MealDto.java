package jy.WorkOutwithAgent.Meal.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "MealDto", description = "식사 정보를 표현하는 응답 모델")
public class MealDto {
    @Schema(description = "식사 고유 식별자", example = "1")
    private Long id;
    @Schema(description = "해당 식사의 회원 식별자", example = "1")
    private Long memberId;
    @Schema(description = "식사 타입", example = "BREAKFAST, LUNCH, DINNER, SNACK")
    private MealType mealType;
    @Schema(description = "식사명", example = "짜장면 부대찌개 마카롱 한정식")
    private String foodName;
    @Schema(description = "섭취 칼로리", example = "285.5")
    private Double calories;
    @Schema(description = "프로틴 그램(g)단위", example = "5.2")
    private Double protein;
    @Schema(description = "탄수화물 그램(g)단위", example = "50.5")
    private Double carbohydrates;
    @Schema(description = "지방 그램(g)단위", example = "150.4")
    private Double fat;
    @Schema(description = "식사 날짜", example = "2025-11-11")
    private LocalDateTime mealDate;
    @Schema(description = "노트", example = "매우 맛있었다. 너무 과식했다. 운동끝나고 먹었다.")
    private String notes;
    @Schema(description = "식사 이미지", example = "http://아마존s3.com/맛있는식사주소")
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