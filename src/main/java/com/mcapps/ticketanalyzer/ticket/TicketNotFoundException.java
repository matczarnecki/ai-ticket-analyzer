package com.mcapps.ticketanalyzer.ticket;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(Long id) {
        super("Ticket with id " + id + "was not found");
    }
}
