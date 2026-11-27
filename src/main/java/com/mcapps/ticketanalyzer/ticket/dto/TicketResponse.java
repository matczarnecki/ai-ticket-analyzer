package com.mcapps.ticketanalyzer.ticket.dto;

import com.mcapps.ticketanalyzer.ticket.SuggestedTeam;
import com.mcapps.ticketanalyzer.ticket.TicketCategory;
import com.mcapps.ticketanalyzer.ticket.TicketSeverity;
import com.mcapps.ticketanalyzer.ticket.TicketStatus;
import java.time.OffsetDateTime;
import java.util.List;

public record TicketResponse(
    Long id,
    String description,
    OffsetDateTime createdAt,
    TicketStatus status,
    String title,
    TicketCategory category,
    TicketSeverity severity,
    String summary,
    List<String> missingInformation,
    SuggestedTeam suggestedTeam,
    OffsetDateTime analyzedAt
) {
}
