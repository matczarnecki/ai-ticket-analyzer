package com.mcapps.ticketanalyzer.ticket;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record TicketAnalysis(
    @Schema(description = "Short factual title", minLength = 1, maxLength = 120)
    String title,

    @Schema(description = "Kind of ticket")
    TicketCategory category,

    @Schema(description = "Technical impact, not business priority")
    TicketSeverity severity,

    @Schema(description = "Factual summary of only what the ticket says", minLength = 1, maxLength = 1000)
    String summary,

    @ArraySchema(
        maxItems = 10,
        schema = @Schema(description = "Something the ticket does not say", maxLength = 100)
    )
    List<String> missingInformation,

    @Schema(description = "Team that should look at this. UNKNOWN when it is unclear")
    SuggestedTeam suggestedTeam
) {
}
