package com.mcapps.ticketanalyzer.ticket.persistence;

import com.mcapps.ticketanalyzer.ticket.SuggestedTeam;
import com.mcapps.ticketanalyzer.ticket.TicketCategory;
import com.mcapps.ticketanalyzer.ticket.TicketSeverity;
import com.mcapps.ticketanalyzer.ticket.TicketStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "tickets")
public class TicketEntity {

    protected TicketEntity() {
    }

    public TicketEntity(String description) {
        this(description, TicketStatus.NEW);
    }

    public TicketEntity(String description, TicketStatus status) {
        this.description = description;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "TEXT")
    private TicketStatus status;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "TEXT")
    private TicketCategory category;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "TEXT")
    private TicketSeverity severity;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "missing_information", columnDefinition = "jsonb")
    private List<String> missingInformation;

    @Enumerated(EnumType.STRING)
    @Column(name = "suggested_team", columnDefinition = "TEXT")
    private SuggestedTeam suggestedTeam;

    @Column(name = "analyzed_at")
    private OffsetDateTime analyzedAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public TicketCategory getCategory() {
        return category;
    }

    public TicketSeverity getSeverity() {
        return severity;
    }

    public String getSummary() {
        return summary;
    }

    public List<String> getMissingInformation() {
        return missingInformation;
    }

    public SuggestedTeam getSuggestedTeam() {
        return suggestedTeam;
    }

    public OffsetDateTime getAnalyzedAt() {
        return analyzedAt;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(TicketCategory category) {
        this.category = category;
    }

    public void setSeverity(TicketSeverity severity) {
        this.severity = severity;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setMissingInformation(List<String> missingInformation) {
        this.missingInformation = missingInformation;
    }

    public void setSuggestedTeam(SuggestedTeam suggestedTeam) {
        this.suggestedTeam = suggestedTeam;
    }

    public void setAnalyzedAt(OffsetDateTime analyzedAt) {
        this.analyzedAt = analyzedAt;
    }
}
