package com.example.demo.services;

import com.example.demo.entities.Week;
import com.example.demo.repository.WeekRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TicketsServiceImplTest {
    private static final Integer TICKETS = 100;
    @Autowired
    TicketsService ticketsService;
    @Autowired
    WeekRepository weekRepository;

    @BeforeEach
    void setUp() {
        for(Week week : weekRepository.findAll()) {
            weekRepository.delete(week);
        }
        ticketsService.createTickets(LocalDate.now(), TICKETS);
    }

    @Test
    void checkEmptyTicketsQuarter() {
        assertEquals(0, ticketsService.getTickets(LocalDate.now().minusYears(1)).size());
    }

    @Test
    void checkCorrectTicketsQuarter() {
        assertTrue(ticketsService.getTickets(LocalDate.now()).size() > 0);
    }

    @Test
    void modify() {
        LocalDate now = LocalDate.now();
        Map<String, Integer> result = ticketsService.getTickets(now);
        Optional<Map.Entry<String, Integer>> optional = result.entrySet().stream().findFirst();
        int valueBefore = 0;
        if(optional.isPresent()) {
            valueBefore = optional.get().getValue();
        }
        assertTrue(valueBefore > 0);

        ticketsService.modify(now, 100);
        result = ticketsService.getTickets(now);
        optional = result.entrySet().stream().findFirst();
        int valueAfter = 0;
        if(optional.isPresent()) {
            valueAfter = optional.get().getValue();
        }
        assertTrue(valueAfter > 0);

        assertTrue(valueAfter > valueBefore);
    }

    @Test
    void modifyZero() {
        LocalDate now = LocalDate.now();
        Map<String, Integer> result = ticketsService.getTickets(now);
        Optional<Map.Entry<String, Integer>> optional = result.entrySet().stream().findFirst();
        int valueBefore = 0;
        if(optional.isPresent()) {
            valueBefore = optional.get().getValue();
        }
        assertTrue(valueBefore > 0);

        ticketsService.modify(now, -10000);
        result = ticketsService.getTickets(now);
        optional = result.entrySet().stream().findFirst();
        int valueAfter = 0;
        if(optional.isPresent()) {
            valueAfter = optional.get().getValue();
        }
        assertTrue(valueAfter == 0);
    }

    @Test
    void createTickets() {
        Map<String, Integer> result = ticketsService.getTickets(LocalDate.now());
        Optional<Map.Entry<String, Integer>> optional = result.entrySet().stream().findFirst();
        int value = 0;
        if(optional.isPresent()) {
            value = optional.get().getValue();
        }
        assertTrue(value > 0);
    }
}