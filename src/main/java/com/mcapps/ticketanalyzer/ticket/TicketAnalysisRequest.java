package com.mcapps.ticketanalyzer.ticket;

import jakarta.validation.constraints.NotBlank;

public record TicketAnalysisRequest(
    @NotBlank(message = "Ticket description cannot be empty")
    String description
) {
}
