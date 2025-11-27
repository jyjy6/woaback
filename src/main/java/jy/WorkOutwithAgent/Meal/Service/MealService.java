package jy.WorkOutwithAgent.Meal.Service;


import jy.WorkOutwithAgent.Auth.Util.AuthUtils;
import jy.WorkOutwithAgent.Meal.DTO.MealRequestDto;
import jy.WorkOutwithAgent.Meal.DTO.MealResponseDto;
import jy.WorkOutwithAgent.Meal.Entity.Meal;
import jy.WorkOutwithAgent.Meal.Repository.MealRepository;
import jy.WorkOutwithAgent.Member.Entity.Member;
import jy.WorkOutwithAgent.Member.Repository.MemberRepository;
import jy.WorkOutwithAgent.Member.Service.CustomUserDetails;
import jy.WorkOutwithAgent.Member.exception.MemberNotFoundException;
import jy.WorkOutwithAgent.Workout.DTO.WorkoutResponseDto;
import jy.WorkOutwithAgent.Workout.Entity.Workout;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class MealService {
    private final MealRepository mealRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Meal createMeal(MealRequestDto mealRequestDto, CustomUserDetails userDetails) {
        AuthUtils.validateMemberId(mealRequestDto.getMemberId(), userDetails);

        Member member = memberRepository.findById(mealRequestDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(mealRequestDto.getMemberId()));

        Meal meal = Meal.builder()
                .member(member)
                .mealType(mealRequestDto.getMealType())
                .foodName(mealRequestDto.getFoodName())
                .calories(mealRequestDto.getCalories())
                .protein(mealRequestDto.getProtein())
                .carbohydrates(mealRequestDto.getCarbohydrates())
                .fat(mealRequestDto.getFat())
                .mealDate(mealRequestDto.getMealDate() != null ? mealRequestDto.getMealDate() : LocalDateTime.now())
                .notes(mealRequestDto.getNotes())
                .imageUrl(mealRequestDto.getImageUrl())
                .build();

        return mealRepository.save(meal);
    }


    @Transactional(readOnly = true)
    public List<MealResponseDto> recentMeals(Long memberId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Meal> workouts = mealRepository.findByMemberIdOrderByMealDateDesc(memberId, pageable);

        return workouts.stream()
                .map(MealResponseDto::fromEntity)
                .toList();
    }


}
