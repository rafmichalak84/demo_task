package com.example.demo.services;

import com.example.demo.entities.Week;
import com.example.demo.repository.WeekRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DateServiceTest {
    @Autowired
    private WeekRepository weekRepository;
    @Autowired
    private TicketsService ticketsService;
    @Autowired
    private DateService dateService;

    @Test
    public void testModifyWeek() {
        List<Week> weeks = dateService.getWeeks(LocalDate.now());
        if(CollectionUtils.isEmpty(weeks))
        {
            ticketsService.createTickets(LocalDate.now(), 100);
            weeks = dateService.getWeeks(LocalDate.now());
        }

        Week week = weeks.stream().findFirst().get();
        int ticketsBefore = week.getTickets();
        dateService.modifyWeek(week, 1);
        int ticketsAfter = week.getTickets();

        assertTrue((ticketsBefore+1) == ticketsAfter);
    }
}