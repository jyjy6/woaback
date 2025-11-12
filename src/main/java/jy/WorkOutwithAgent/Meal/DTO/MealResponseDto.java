package jy.WorkOutwithAgent.Meal.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jy.WorkOutwithAgent.Meal.Entity.Meal;
import jy.WorkOutwithAgent.Meal.Entity.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealResponseDto {
    @Schema(description = "식사 기록 고유 식별자", example = "1")
    private Long id;

    @Schema(description = "식사를 한 회원의 고유식별자", example = "1")
    private Long memberId;

    @Schema(description = "식사 종류", example = "LUNCH")
    private MealType mealType;

    @Schema(description = "음식 이름", example = "닭가슴살 샐러드")
    private String foodName;

    @Schema(description = "칼로리", example = "350.5")
    private Double calories;

    @Schema(description = "단백질 (g)", example = "40.0")
    private Double protein;

    @Schema(description = "탄수화물 (g)", example = "15.5")
    private Double carbohydrates;

    @Schema(description = "지방 (g)", example = "12.0")
    private Double fat;

    @Schema(description = "식사 날짜와 시간", example = "2025-11-12T12:30:00")
    private LocalDateTime mealDate;

    @Schema(description = "메모", example = "드레싱 없이 먹음")
    private String notes;

    @Schema(description = "음식 사진 URL", example = "http://example.com/image.jpg")
    private String imageUrl;

    public static MealResponseDto fromEntity(Meal meal) {
        return MealResponseDto.builder()
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
