package jy.WorkOutwithAgent.Workout.Entity;


import jakarta.persistence.*;
import jy.WorkOutwithAgent.Member.Entity.Member;
import jy.WorkOutwithAgent.Workout.Entity.enums.Intensity;
import jy.WorkOutwithAgent.Workout.Entity.enums.WorkoutType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private WorkoutType workoutType;

    private String exerciseName;
    private Intensity intensity;

    private Integer sets;
    private Integer reps;
    private Double weight; // in kg or lbs

    private Integer durationMinutes;
    private Double distanceKm;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime workoutDate;

    private String notes;
}
