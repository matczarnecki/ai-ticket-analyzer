package com.mcapps.ticketanalyzer.ticket.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
}
