package com.mcapps.ticketanalyzer.ticket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.mcapps.ticketanalyzer.ticket.persistence.TicketEntity;
import com.mcapps.ticketanalyzer.ticket.persistence.TicketRepository;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = "app.ticket-analysis.fixed-delay=1h")
class TicketAnalysisJobTest {

    @Autowired
    private TicketAnalysisJob ticketAnalysisJob;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockitoBean
    private TicketAnalysisService ticketAnalysisService;

    private final List<Long> createdIds = new ArrayList<>();

    @AfterEach
    void deleteCreatedTickets() {
        createdIds.forEach(ticketRepository::deleteById);
    }

    @Test
    void savesAnalyzedTicketWhenTheModelReturnsAnalysis() {
        Long ticketId = createOldestNewTicket("Saving a user profile returns HTTP 500.");
        TicketAnalysis analysis = new TicketAnalysis(
            "Profile save returns 500",
            TicketCategory.BUG,
            TicketSeverity.HIGH,
            "Every attempt to save a user profile returns HTTP 500.",
            List.of("stack trace"),
            SuggestedTeam.BACKEND
        );
        when(ticketAnalysisService.analyze("Saving a user profile returns HTTP 500.")).thenReturn(analysis);

        ticketAnalysisJob.analyzeNextTicket();

        TicketEntity stored = ticketRepository.findById(ticketId).orElseThrow();
        assertThat(stored.getStatus()).isEqualTo(TicketStatus.ANALYZED);
        assertThat(stored.getTitle()).isEqualTo("Profile save returns 500");
        assertThat(stored.getCategory()).isEqualTo(TicketCategory.BUG);
        assertThat(stored.getSeverity()).isEqualTo(TicketSeverity.HIGH);
        assertThat(stored.getSummary()).isEqualTo("Every attempt to save a user profile returns HTTP 500.");
        assertThat(stored.getMissingInformation()).containsExactly("stack trace");
        assertThat(stored.getSuggestedTeam()).isEqualTo(SuggestedTeam.BACKEND);
        assertThat(stored.getAnalyzedAt()).isNotNull();
    }

    @Test
    void marksTicketFailedWhenAnalysisThrows() {
        Long ticketId = createOldestNewTicket("Saving a user profile returns HTTP 500.");
        when(ticketAnalysisService.analyze("Saving a user profile returns HTTP 500."))
            .thenThrow(new RuntimeException("analysis failed"));

        ticketAnalysisJob.analyzeNextTicket();

        TicketEntity stored = ticketRepository.findById(ticketId).orElseThrow();
        assertThat(stored.getStatus()).isEqualTo(TicketStatus.FAILED);
        assertThat(stored.getTitle()).isNull();
        assertThat(stored.getAnalyzedAt()).isNull();
        assertThat(stored.getDescription()).isEqualTo("Saving a user profile returns HTTP 500.");
    }

    private Long createOldestNewTicket(String description) {
        TicketEntity ticket = ticketRepository.save(new TicketEntity(description));
        createdIds.add(ticket.getId());

        Timestamp oldest = jdbcTemplate.queryForObject(
            "select min(created_at) from tickets",
            Timestamp.class
        );
        OffsetDateTime createdAt = oldest == null
            ? OffsetDateTime.parse("1970-01-01T00:00:00Z")
            : oldest.toInstant().atOffset(ZoneOffset.UTC).minusSeconds(1);

        jdbcTemplate.update(
            "update tickets set created_at = ? where id = ?",
            Timestamp.from(createdAt.toInstant()),
            ticket.getId()
        );

        return ticket.getId();
    }
}
