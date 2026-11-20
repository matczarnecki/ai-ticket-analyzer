package com.mcapps.ticketanalyzer.controller;

import com.mcapps.ticketanalyzer.ticket.TicketAnalysis;
import com.mcapps.ticketanalyzer.ticket.TicketAnalysisRequest;
import com.mcapps.ticketanalyzer.ticket.TicketAnalysisService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketAnalysisController {

    private final TicketAnalysisService ticketAnalysisService;

    public TicketAnalysisController(TicketAnalysisService ticketAnalysisService) {
        this.ticketAnalysisService = ticketAnalysisService;
    }

    @PostMapping("/analyze")
    public TicketAnalysis analyze(
        @Valid @RequestBody TicketAnalysisRequest request
    ) {
        return ticketAnalysisService.analyze(request.description());
    }
}
