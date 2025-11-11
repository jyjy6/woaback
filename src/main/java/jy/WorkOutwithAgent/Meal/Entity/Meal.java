package jy.WorkOutwithAgent.Meal.Entity;


import jakarta.persistence.*;
import jy.WorkOutwithAgent.Member.Entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealType mealType;

    @Column(nullable = false)
    private String foodName;

    private Double calories;
    private Double protein;
    private Double carbohydrates;
    private Double fat;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime mealDate;

    private String notes;
    private String imageUrl;
}