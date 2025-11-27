package jy.WorkOutwithAgent.Meal.Service;

import jy.WorkOutwithAgent.GlobalErrorHandler.GlobalException;
import jy.WorkOutwithAgent.Meal.DTO.MacroRatioDto;
import jy.WorkOutwithAgent.Meal.DTO.NutritionSummaryDto;
import jy.WorkOutwithAgent.Meal.Entity.Meal;
import jy.WorkOutwithAgent.Meal.Repository.MealRepository;
import jy.WorkOutwithAgent.Member.Entity.Member;
import jy.WorkOutwithAgent.Member.Repository.MemberRepository;
import jy.WorkOutwithAgent.Workout.Entity.Workout;
import jy.WorkOutwithAgent.Workout.Entity.enums.Intensity;
import jy.WorkOutwithAgent.Workout.Entity.enums.WorkoutType;
import jy.WorkOutwithAgent.Workout.Repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NutritionService {

    private final MemberRepository memberRepository;
    private final WorkoutRepository workoutRepository;
    private final MealRepository mealRepository;

    @Transactional(readOnly = true)
    public NutritionSummaryDto getTodaySummary(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException("사용자를 찾을 수 없습니다", "MEMBER_NOT_FOUND", HttpStatus.NOT_FOUND));
        List<Meal> todayMeals = mealRepository.findTodayMeals(memberId);
        List<Workout> todayWorkouts = workoutRepository.findTodayWorkouts(memberId);

        Double totalCalories = todayMeals.stream()
                .mapToDouble(Meal::getCalories).sum();
        Double totalProtein = todayMeals.stream()
                .mapToDouble(Meal::getProtein).sum();
        Double totalCarbs = todayMeals.stream()
                .mapToDouble(Meal::getCarbohydrates).sum();
        Double totalFat = todayMeals.stream()
                .mapToDouble(Meal::getFat).sum();

        Double burnedCalories = estimateWorkoutCalories(member, todayWorkouts);
        Double recommendedCalories = calculateRecommendedCalories(member);
        MacroRatioDto recommendedRatio = calculateRecommendedMacros(member);

        return new NutritionSummaryDto(
                totalCalories,
                burnedCalories,
                totalProtein,
                totalCarbs,
                totalFat,
                recommendedCalories,
                recommendedRatio.getProtein(),
                recommendedRatio.getCarbs(),
                recommendedRatio.getFat()
        );
    }



/**
 * ⚙️ 칼로리 계산 아이디어
 * 운동 소모 칼로리는 일반적으로
 * 🔸 MET(운동강도) × 체중(kg) × 시간(h) × 1.05
 * 으로 계산합니다.
 *
 * MET: 운동의 강도 지수 (예: 걷기 3.5, 달리기 9.0, 근력운동 6.0 등)
 *
 * 시간: 분 단위 → 시간으로 변환 (예: 30분 → 0.5시간)
 * 1.05는 체온, 회복 등 추가적인 에너지 소모를 약간 보정하기 위한 경험적 계수
 * Intensity(0~10)과 WorkoutType을 조합해서 MET 추정값을 계산.
 *
 * */

    public Double estimateWorkoutCalories(Member member, List<Workout> todayWorkouts) {
        if (todayWorkouts == null || todayWorkouts.isEmpty()) return 0.0;

        Double totalCalories = 0.0;
        Double weight = member.getWeight(); // kg 기준

        for (Workout workout : todayWorkouts) {
            Double durationHours = (workout.getDurationMinutes() != null ? workout.getDurationMinutes() : 0) / 60.0;
            if (durationHours <= 0) continue;

            Double met = estimateMET(workout.getWorkoutType(), workout.getIntensity());
            totalCalories += met * weight * durationHours * 1.05;
        }

        return Math.round(totalCalories * 10) / 10.0; // 소수점 1자리 반올림
    }

    private Double estimateMET(WorkoutType type, Intensity intensity) {
        int level = intensity.getValue(); // 0~10
        Double baseMET;

        switch (type) {
            case CARDIO:
                baseMET = 4.0 + (level * 0.6);  // 예: 4~10
                break;
            case STRENGTH_TRAINING:
                baseMET = 3.5 + (level * 0.4);  // 예: 3.5~7.5
                break;
            case SPORTS:
                baseMET = 5.0 + (level * 0.5);  // 예: 5~10
                break;
            case FLEXIBILITY:
                baseMET = 2.0 + (level * 0.3);  // 예: 2~5
                break;
            default:
                baseMET = 3.0;
        }

        return baseMET;
    }

    /**
     * 🔹 계산 공식
     *
     * 1. BMR (기초대사량)
     * 남성: BMR = 10 * weight(kg) + 6.25 * height(cm) - 5 * age + 5
     * 여성: BMR = 10 * weight(kg) + 6.25 * height(cm) - 5 * age - 161
     *
     * 2. 활동 계수(Activity Factor)
     * (기본 1.2로 둡니다.)
     *
     * */
    public Double calculateRecommendedCalories(Member member){
        if (member == null || member.getWeight() == null || member.getHeight() == null || member.getAge() == null) {
            throw new IllegalArgumentException("Member information incomplete for calorie calculation.");
        }

        Double weight = member.getWeight();  // kg
        Double height = member.getHeight();  // cm
        Integer age = member.getAge();
        String sex = member.getSex();

        // 기본 활동 계수 (운동 수준 필드가 없다면 1.2로 둠)
        Double activityFactor = 1.2;

        // 1️⃣ BMR 계산
        Double bmr;
        if ("male".equalsIgnoreCase(sex) || "남".equalsIgnoreCase(sex) || "남자".equalsIgnoreCase(sex)) {
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else if ("female".equalsIgnoreCase(sex) || "여".equalsIgnoreCase(sex) || "여자".equalsIgnoreCase(sex)) {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        } else {
            // 성별 미입력 시 평균값 적용
            bmr = 10 * weight + 6.25 * height - 5 * age;
        }
        // 2️⃣ 활동계수 반영 (TDEE)
        Double recommendedCalories = bmr * activityFactor;
        // 소수점 한 자리로 반올림
        return Math.round(recommendedCalories * 10) / 10.0;
    }

    public MacroRatioDto calculateRecommendedMacros(Member member) {
        // 기본 비율 (%)
        Double proteinRatio = 0.2;
        Double carbRatio = 0.5;
        Double fatRatio = 0.3;

        // 하루 권장 칼로리 계산
        Double recommendedCalories = calculateRecommendedCalories(member);

        // 각 매크로별 g 계산 (1g = protein 4kcal, carb 4kcal, fat 9kcal)
        Double proteinGrams = (recommendedCalories * proteinRatio) / 4.0;
        Double carbGrams = (recommendedCalories * carbRatio) / 4.0;
        Double fatGrams = (recommendedCalories * fatRatio) / 9.0;

        return new MacroRatioDto(proteinGrams, carbGrams, fatGrams);
    }
}
