package com.mcapps.ticketanalyzer.ticket;

import java.util.List;

public record TicketAnalysis(
    String title,
    TicketCategory category,
    TicketSeverity severity,
    String summary,
    List<String> missingInformation,
    SuggestedTeam suggestedTeam
) {
}
