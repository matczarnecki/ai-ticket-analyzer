package com.mcapps.ticketanalyzer.ticket;

import com.mcapps.ticketanalyzer.ticket.persistence.TicketEntity;
import com.mcapps.ticketanalyzer.ticket.persistence.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TicketAnalysisJob {

    private static final Logger log = LoggerFactory.getLogger(TicketAnalysisJob.class);

    private final TicketRepository ticketRepository;
    private final TicketAnalysisService ticketAnalysisService;
    private final TicketAnalysisResultService ticketAnalysisResultService;

    public TicketAnalysisJob(
        TicketRepository ticketRepository,
        TicketAnalysisService ticketAnalysisService,
        TicketAnalysisResultService ticketAnalysisResultService
    ) {
        this.ticketRepository = ticketRepository;
        this.ticketAnalysisService = ticketAnalysisService;
        this.ticketAnalysisResultService = ticketAnalysisResultService;
    }

    @Scheduled(fixedDelayString = "${app.ticket-analysis.fixed-delay}")
    public void analyzeNextTicket() {
        TicketEntity ticket = ticketRepository
            .findFirstByStatusOrderByCreatedAtAscIdAsc(TicketStatus.NEW)
            .orElse(null);

        if (ticket == null) {
            return;
        }

        Long ticketId = ticket.getId();

        try {
            TicketAnalysis analysis = ticketAnalysisService.analyze(ticket.getDescription());
            ticketAnalysisResultService.saveAnalyzed(ticketId, analysis);
        }
        catch (RuntimeException error) {
            ticketAnalysisResultService.markFailed(ticketId);
            log.error("Ticket {} marked FAILED", ticketId, error);
        }
    }
}
