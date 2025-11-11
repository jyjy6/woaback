package jy.WorkOutwithAgent.Meal.DTO;


import lombok.Data;

@Data
public class MacroRatioDto {
    private double protein;       // 단백질(g) 또는 %
    private double carbs;         // 탄수화물(g) 또는 %
    private double fat;           // 지방(g) 또는 %

    public MacroRatioDto(double protein, double carbs, double fat) {
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public double getProtein() { return protein; }
    public double getCarbs() { return carbs; }
    public double getFat() { return fat; }
}
