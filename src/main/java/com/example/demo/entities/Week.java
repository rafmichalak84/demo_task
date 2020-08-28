package com.example.demo.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Week {
    @EmbeddedId
    private WeekId id;
    private int tickets;

    public WeekId getId() {
        return id;
    }

    public void setId(WeekId id) {
        this.id = id;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }
}
