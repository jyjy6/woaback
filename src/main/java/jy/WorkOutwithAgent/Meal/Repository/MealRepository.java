package jy.WorkOutwithAgent.Meal.Repository;

import jy.WorkOutwithAgent.Meal.Entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query("SELECT m FROM Meal m WHERE m.member.id = :memberId AND DATE(m.mealDate) = CURRENT_DATE")
    List<Meal> findTodayMeals(@Param("memberId") Long memberId);
}
