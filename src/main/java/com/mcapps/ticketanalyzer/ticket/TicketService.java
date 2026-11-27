
package com.mcapps.ticketanalyzer.ticket;

import com.mcapps.ticketanalyzer.ticket.dto.CreateTicketRequest;
import com.mcapps.ticketanalyzer.ticket.dto.TicketResponse;
import com.mcapps.ticketanalyzer.ticket.persistence.TicketEntity;
import com.mcapps.ticketanalyzer.ticket.persistence.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public TicketResponse create(CreateTicketRequest request) {
        TicketEntity ticket = new TicketEntity(request.description());

        TicketEntity savedTicket = ticketRepository.save(ticket);

        return toResponse(savedTicket);
    }

    @Transactional(readOnly = true)
    public TicketResponse findById(Long id) {
        TicketEntity ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new TicketNotFoundException(id));

        return toResponse(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> findAll() {
        return ticketRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    private TicketResponse toResponse(TicketEntity ticket) {
        return new TicketResponse(
            ticket.getId(),
            ticket.getDescription(),
            ticket.getCreatedAt(),
            ticket.getStatus(),
            ticket.getTitle(),
            ticket.getCategory(),
            ticket.getSeverity(),
            ticket.getSummary(),
            ticket.getMissingInformation(),
            ticket.getSuggestedTeam(),
            ticket.getAnalyzedAt()
        );
    }
}
