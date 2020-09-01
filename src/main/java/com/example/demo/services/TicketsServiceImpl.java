package com.example.demo.services;

import com.example.demo.entities.Week;
import com.example.demo.entities.WeekId;
import com.example.demo.repository.WeekRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
class TicketsServiceImpl implements TicketsService {
    private final static String WEEK = "week";
    private WeekRepository weekRepository;
    private DateService dateService;

    public TicketsServiceImpl(WeekRepository weekRepository, DateService dateService) {
        this.weekRepository = weekRepository;
        this.dateService = dateService;
    }

    @Override
    public Map<String, Integer> getTickets(LocalDate date) {
        return dateService.getWeeks(date)
                .stream()
                .collect(Collectors.toMap(week -> WEEK+week.getId().getWeek(), Week::getTickets));
    }

    @Override
    public void modify(LocalDate date, Integer tickets) {
        int weeksPerQuater = dateService.howManyWeeksInQuarter(date);
        int ticketsPerWeek = tickets/weeksPerQuater;
        int ticketsWithoutWeek = tickets%weeksPerQuater;

        List<Week> sortedList = dateService.getWeeks(date)
                .stream()
                .sorted(comparing(Week::getTickets))
                .collect(Collectors.toList());

        if(ticketsWithoutWeek < 0) {
            Collections.reverse(sortedList);
        }

        for(Week week : sortedList) {
            int weekTickets = ticketsPerWeek;
            if(ticketsWithoutWeek < 0) {
                weekTickets--;
                ticketsWithoutWeek++;
            } else if(ticketsWithoutWeek > 0) {
                weekTickets++;
                ticketsWithoutWeek--;
            }
            dateService.modifyWeek(week, weekTickets);
        }
    }

    @Override
    public void createTickets(LocalDate date, Integer tickets) {
        int weeksPerQuater = dateService.howManyWeeksInQuarter(date);
        int ticketsPerWeek = tickets/weeksPerQuater;
        int ticketsWithoutWeek = tickets%weeksPerQuater;

        for(int weekNo = dateService.weekOfDate(dateService.firstDayOfQuarter(date));
            weekNo <= dateService.weekOfDate(dateService.lastDayOfQuarter(date));
            weekNo++) {

            int weekTickets = ticketsPerWeek;
            if(ticketsWithoutWeek>0) {
                ticketsWithoutWeek--;
                weekTickets++;
            }
            createWeekOfNotExists(date, weekNo, weekTickets);
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

}
