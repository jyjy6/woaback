package jy.WorkOutwithAgent.Member.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jy.WorkOutwithAgent.Meal.DTO.MealDto;
import jy.WorkOutwithAgent.Member.Entity.Member;
import jy.WorkOutwithAgent.Workout.DTO.WorkoutDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberDto {
    private Long id;
    private String username;
    private String name;
    private String displayName;
    private String email;
    private String phone;
    private Double height;
    private Double weight;
    private String sex;
    private Integer age;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;

    @JsonProperty("isPremium")
    private boolean isPremium;
    private LocalDateTime premiumExpiryDate;

    private boolean marketingAccepted;
    private Set<String> roleSet;
    private List<MealDto> meals;
    private List<WorkoutDto> workouts;


    public static MemberDto convertToDetailMemberDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .displayName(member.getDisplayName())
                .email(member.getEmail())
                .age(member.getAge())
                .sex(member.getSex())
                .height(member.getHeight())
                .weight(member.getWeight())
                .phone(member.getPhone())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .isPremium(member.isPremium())
                .lastLogin(member.getLastLogin())
                .premiumExpiryDate(member.getPremiumExpiryDate())
                .marketingAccepted(member.isMarketingAccepted())
                .roleSet(member.getRoles())
//                .meals(member.getMeals().stream().map(MealDto::fromEntity).collect(Collectors.toList()))
//                .workouts(member.getWorkouts().stream().map(WorkoutDto::fromEntity).collect(Collectors.toList()))
                .build();
    }


}