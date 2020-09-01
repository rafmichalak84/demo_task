package com.example.demo.services;

import com.example.demo.entities.Week;
import com.example.demo.repository.WeekRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class DateService {

    private WeekRepository weekRepository;

    public DateService(WeekRepository weekRepository) {
        this.weekRepository = weekRepository;
    }

    public void modifyWeek(Week week, int tickets) {
        int tmpTickets = week.getTickets() + tickets;
        if(tmpTickets < 0) {
            tmpTickets = 0;
        }
        week.setTickets(tmpTickets);
        weekRepository.save(week);
    }

    public List<Week> getWeeks(LocalDate date) {
        return weekRepository.getAllBetweenWeeks(date.getYear(),
                weekOfDate(firstDayOfQuarter(date)),
                weekOfDate(lastDayOfQuarter(date)));
    }

    public int weekOfDate(LocalDate date) {
        return date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    public LocalDate firstDayOfQuarter(LocalDate date) {
        return date.with(date.getMonth().firstMonthOfQuarter())
                .with(TemporalAdjusters.firstDayOfMonth());
    }

    public LocalDate lastDayOfQuarter(LocalDate date) {
        return firstDayOfQuarter(date).plusMonths(2)
                .with(TemporalAdjusters.lastDayOfMonth());
    }

    public int howManyWeeksInQuarter(LocalDate date) {
        return weekOfDate(lastDayOfQuarter(date)) - weekOfDate(firstDayOfQuarter(date));
    }

}
