package com.mcapps.ticketanalyzer.embedding;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("integration")
@SpringBootTest
public class EmbeddingServiceIntegrationTest {

    @Autowired
    private EmbeddingService embeddingService;

    @Test
    void shouldGenerateEmbedding() {
        float[] embedding = embeddingService.embed(
            "Users cannot log in to the application."
        );

        assertThat(embedding)
            .isNotNull()
            .hasSize(1024);
    }
}
