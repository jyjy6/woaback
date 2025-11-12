package jy.WorkOutwithAgent.Workout.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jy.WorkOutwithAgent.Workout.Entity.enums.Intensity;
import jy.WorkOutwithAgent.Workout.Entity.enums.WorkoutType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkoutRequestDto {
    @Schema(description = "운동을 한 회원의 고유식별자", example = "1")
    private Long memberId;
    @Schema(description = "운동 타입",
            example = "STRENGTH_TRAINING(스트렝스), CARDIO(카디오)," +
            "FLEXIBILITY(유연성), SPORTS(스포츠)")
    private WorkoutType workoutType;
    @Schema(description = "운동 이름", example = "런닝")
    private String exerciseName;
    @Schema(description = "실행 세트", example = "12")
    private Integer sets;
    @Schema(description = "반복횟수", example = "12")
    private Integer reps;
    @Schema(description = "중량", example = "125.5kg")
    private Double weight;
    @Schema(description = "총 걸린 시간", example = "50")
    private Integer durationMinutes;
    @Schema(description = "총 걸린 거리 km 단위 (런닝 등)", example = "40")
    private Double distanceKm;
    @Schema(description = "운동한 날짜", example = "2025-11-11")
    private LocalDateTime workoutDate;
    @Schema(description = "강도", example = "")
    private Intensity intensity;
    @Schema(description = "메모", example = "빡운동 했다.")
    private String notes;

}
