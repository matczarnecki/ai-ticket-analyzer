package com.mcapps.ticketanalyzer.ticket.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTicketRequest(
    @NotBlank
    String description
) {
}
