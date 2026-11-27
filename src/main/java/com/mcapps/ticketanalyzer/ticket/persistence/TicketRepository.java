package com.mcapps.ticketanalyzer.ticket.persistence;

import com.mcapps.ticketanalyzer.ticket.TicketStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    Optional<TicketEntity> findFirstByStatusOrderByCreatedAtAscIdAsc(TicketStatus status);
}
