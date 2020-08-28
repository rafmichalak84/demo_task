package com.example.demo.services;

import java.time.LocalDate;
import java.util.Map;

public interface TicketsService {

    Map<String, Integer> getTickets(LocalDate date);

    void modify(LocalDate date, Integer tickets);

    void createTickets(LocalDate now, Integer tickets);

}
