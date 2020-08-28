package com.example.demo.controllers;

import com.example.demo.services.TicketsService;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/settlements")
public class SettlementsController {
    private TicketsService ticketsService;

    public SettlementsController(TicketsService ticketsService) {
        this.ticketsService = ticketsService;
    }

    @GetMapping(path = "/tickets",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> getTicketsListInCurrentQuarter() {
        return getTicketsListInQuarter(LocalDate.now());
    }

    @PostMapping(path = "/tickets/create",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean createTicketsInQuarter(@RequestParam Integer tickets) {
        ticketsService.createTickets(LocalDate.now(), tickets);
        return true;
    }

    @GetMapping(path = "/tickets/{date}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> getTicketsListInQuarter(@ApiParam(value = "date", example = "yyyy-MM-dd")
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                            @PathVariable LocalDate date) {
        return ticketsService.getTickets(date);
    }

    @PostMapping(path = "/tickets/{date}/modify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> modifyPromotionTickets(@RequestParam Integer tickets,
                                                       @ApiParam(value = "date", example = "yyyy-mm-dd")
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                       @PathVariable LocalDate date) {
        ticketsService.modify(date, tickets);
        return getTicketsListInQuarter(date);
    }
}
