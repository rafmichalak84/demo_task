package com.example.demo.repository;

import com.example.demo.entities.Week;
import com.example.demo.entities.WeekId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeekRepository extends CrudRepository<Week, WeekId> {

    @Query(value = "from Week k where year = :year AND week BETWEEN :startWeek AND :endWeek")
    List<Week> getAllBetweenWeeks(@Param("year")int year,
                                  @Param("startWeek")int startWeek,
                                  @Param("endWeek")int endWeek);

}
