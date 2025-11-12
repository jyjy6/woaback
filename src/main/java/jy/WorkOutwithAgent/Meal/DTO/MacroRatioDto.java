package jy.WorkOutwithAgent.Meal.DTO;


import lombok.Data;

@Data
public class MacroRatioDto {
    private Double protein;       // 단백질(g) 또는 %
    private Double carbs;         // 탄수화물(g) 또는 %
    private Double fat;           // 지방(g) 또는 %

    public MacroRatioDto(double protein, double carbs, double fat) {
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public Double getProtein() { return protein; }
    public Double getCarbs() { return carbs; }
    public Double getFat() { return fat; }
}
