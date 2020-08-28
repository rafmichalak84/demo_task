package com.example.demo.services;

import com.example.demo.entities.Week;
import com.example.demo.entities.WeekId;
import com.example.demo.repository.WeekRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
class TicketsServiceImpl implements TicketsService {
    private final static String WEEK = "week";
    private WeekRepository weekRepository;

    public TicketsServiceImpl(WeekRepository weekRepository) {
        this.weekRepository = weekRepository;
    }

    @Override
    public Map<String, Integer> getTickets(LocalDate date) {
        return getWeeks(date)
                .stream()
                .collect(Collectors.toMap(week -> WEEK+week.getId().getWeek(), Week::getTickets));
    }

    @Override
    public void modify(LocalDate date, Integer tickets) {
        int ticketsPerWeek = tickets/howManyWeeksInQuarter(date);

        getWeeks(date).forEach(week -> modifyWeek(week, ticketsPerWeek));
    }

    @Override
    public void createTickets(LocalDate date, Integer tickets) {
        int ticketsPerWeek = ticketsPerWeek(date, tickets);
        for(int weekNo = weekOfDate(firstDayOfQuarter(date));
            weekNo <= weekOfDate(lastDayOfQuarter(date));
            weekNo++) {

            createWeekOfNotExists(date, weekNo, ticketsPerWeek);
        }
    }

    private void createWeekOfNotExists(LocalDate date, int weekNo, int ticketsPerWeek) {
        WeekId weekId = new WeekId(date.getYear(), weekNo);
        if(!weekRepository.findById(weekId).isPresent()) {
            Week week = new Week();
            week.setId(weekId);
            week.setTickets(ticketsPerWeek);
            weekRepository.save(week);
        }
    }

    private List<Week> getWeeks(LocalDate date) {
        return weekRepository.getAllBetweenWeeks(date.getYear(),
                weekOfDate(firstDayOfQuarter(date)),
                weekOfDate(lastDayOfQuarter(date)));
    }

    private void modifyWeek(Week week, int tickets) {
        int tmpTickets = week.getTickets() + tickets;
        if(tmpTickets < 0) {
            tmpTickets = 0;
        }
        week.setTickets(tmpTickets);
        weekRepository.save(week);
    }

    private LocalDate firstDayOfQuarter(LocalDate date) {
        return date.with(date.getMonth().firstMonthOfQuarter())
                .with(TemporalAdjusters.firstDayOfMonth());
    }

    private LocalDate lastDayOfQuarter(LocalDate date) {
        return firstDayOfQuarter(date).plusMonths(2)
                .with(TemporalAdjusters.lastDayOfMonth());
    }

    private int weekOfDate(LocalDate date) {
        return date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    private int howManyWeeksInQuarter(LocalDate date) {
        return weekOfDate(lastDayOfQuarter(date)) - weekOfDate(firstDayOfQuarter(date));
    }

    private int ticketsPerWeek(LocalDate date, int tickets) {
        return tickets/howManyWeeksInQuarter(date);
    }
}
