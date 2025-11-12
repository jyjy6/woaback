package jy.WorkOutwithAgent.Workout.Repository;


import jy.WorkOutwithAgent.Workout.Entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {


    @Query("SELECT w FROM Workout w WHERE w.member.id = :memberId AND DATE(w.workoutDate) = CURRENT_DATE")
    List<Workout> findTodayWorkouts(@Param("memberId") Long memberId);

    List<Workout> findByMember_IdAndWorkoutDateBetween(Long memberId, java.time.LocalDateTime startDateTime, java.time.LocalDateTime endDateTime);

}
