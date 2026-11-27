package com.mcapps.ticketanalyzer.ticket;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("integration")
@SpringBootTest(properties = "app.ticket-analysis.fixed-delay=1h")
class TicketAnalysisIntegrationTest {

    @Autowired
    private TicketAnalysisService ticketAnalysisService;

    @Test
    void shouldAnalyzeBackendError() {

        TicketAnalysis result = ticketAnalysisService.analyze("""
            Every attempt to save a user profile returns HTTP 500.
            All users are affected.
            """);

        assertThat(result).isNotNull();
        assertThat(result.title()).isNotBlank();
    }

}
