package com.mcapps.ticketanalyzer.vectorstore;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Calls the auto-configured {@link VectorStore}. This test is not {@code @Transactional}:
 * {@code add} embeds the text in Ollama before {@code JdbcTemplate} writes the row, and
 * {@code similaritySearch} embeds the query before PostgreSQL runs the cosine operator.
 */
@Tag("integration")
@SpringBootTest(properties = "app.ticket-analysis.fixed-delay=1h")
class VectorStoreIntegrationTest {

    private static final String TEXT =
        "Users cannot reset their password because the reset email never arrives.";

    @Autowired
    private VectorStore vectorStore;

    private String documentId;

    @AfterEach
    void deleteStoredDocument() {
        if (documentId != null) {
            vectorStore.delete(List.of(documentId));
        }
    }

    @Test
    void storesOneDocumentAndFindsItBySimilarity() {
        documentId = UUID.randomUUID().toString();
        Document document = Document.builder()
            .id(documentId)
            .text(TEXT)
            .metadata("source", "vector-store-lesson")
            .build();

        vectorStore.add(List.of(document));

        List<Document> matches = vectorStore.similaritySearch(SearchRequest.builder()
            .query("The password reset message is not delivered.")
            .topK(1)
            .build());

        assertThat(matches).singleElement().satisfies(match -> {
            assertThat(match.getId()).isEqualTo(documentId);
            assertThat(match.getText()).isEqualTo(TEXT);
            assertThat(match.getMetadata()).containsEntry("source", "vector-store-lesson");
            assertThat(match.getScore()).isNotNull().isGreaterThan(0.5);
        });
    }
}
