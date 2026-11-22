package com.mcapps.ticketanalyzer.ticket.dto;

import java.time.OffsetDateTime;

public record TicketResponse(
    Long id,
    String description,
    OffsetDateTime createdAt
) {
}
