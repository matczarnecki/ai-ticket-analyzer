package com.mcapps.ticketanalyzer.vectorstore;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Splits one document, then calls the auto-configured {@link VectorStore}.
 * This test is not {@code @Transactional}: splitting is in memory, and {@code add}
 * embeds every chunk in Ollama before {@code JdbcTemplate} writes the rows.
 */
@Tag("integration")
@SpringBootTest(properties = "app.ticket-analysis.fixed-delay=1h")
class ChunkedVectorStoreIntegrationTest {

    private static final String RESET_SENTENCE =
        "Users cannot reset their password because the reset email never arrives.";

    private static final String BACKUP_SENTENCE =
        "The nightly database backup never reaches the offsite vault.";

    private static final String TEXT = """
        %s The login page keeps spinning and the user stays signed out.
        %s Operators only notice the missing archive the next morning.
        """.formatted(RESET_SENTENCE, BACKUP_SENTENCE);

    @Autowired
    private VectorStore vectorStore;

    private List<String> chunkIds = List.of();

    @AfterEach
    void deleteStoredChunks() {
        if (!chunkIds.isEmpty()) {
            vectorStore.delete(chunkIds);
        }
    }

    @Test
    void findsTheChunkThatMatchesTheQuestion() {
        String parentId = UUID.randomUUID().toString();
        Document source = Document.builder()
            .id(parentId)
            .text(TEXT)
            .metadata("source", "chunk-lesson")
            .build();

        TokenTextSplitter splitter = TokenTextSplitter.builder()
            .withChunkSize(20)
            .withChunkOverlap(0)
            .withMinChunkSizeChars(40)
            .build();

        List<Document> chunks = splitter.split(source);
        chunkIds = chunks.stream().map(Document::getId).toList();

        assertThat(chunks).hasSizeGreaterThan(1);
        assertThat(chunks).allSatisfy(chunk ->
            assertThat(chunk.getMetadata()).containsEntry("parent_document_id", parentId)
        );

        vectorStore.add(chunks);

        List<Document> matches = vectorStore.similaritySearch(SearchRequest.builder()
            .query("The password reset message is not delivered.")
            .topK(1)
            .build());

        assertThat(matches).singleElement().satisfies(match -> {
            assertThat(match.getText()).contains("reset email");
            assertThat(match.getText()).doesNotContain("offsite vault");
            assertThat(match.getMetadata()).containsEntry("parent_document_id", parentId);
            assertThat(match.getMetadata()).containsEntry("source", "chunk-lesson");
            assertThat(match.getScore()).isNotNull().isGreaterThan(0.5);
        });
    }
}
