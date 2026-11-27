package com.mcapps.ticketanalyzer.ticket;

import com.mcapps.ticketanalyzer.ticket.persistence.TicketEntity;
import com.mcapps.ticketanalyzer.ticket.persistence.TicketRepository;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketAnalysisResultService {

    private final TicketRepository ticketRepository;

    public TicketAnalysisResultService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public void saveAnalyzed(Long ticketId, TicketAnalysis analysis) {
        TicketEntity ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new TicketNotFoundException(ticketId));

        if (ticket.getStatus() != TicketStatus.NEW) {
            return;
        }

        ticket.setTitle(analysis.title());
        ticket.setCategory(analysis.category());
        ticket.setSeverity(analysis.severity());
        ticket.setSummary(analysis.summary());
        ticket.setMissingInformation(analysis.missingInformation());
        ticket.setSuggestedTeam(analysis.suggestedTeam());
        ticket.setStatus(TicketStatus.ANALYZED);
        ticket.setAnalyzedAt(OffsetDateTime.now());
    }

    @Transactional
    public void markFailed(Long ticketId) {
        TicketEntity ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new TicketNotFoundException(ticketId));

        if (ticket.getStatus() != TicketStatus.NEW) {
            return;
        }

        ticket.setStatus(TicketStatus.FAILED);
    }
}
