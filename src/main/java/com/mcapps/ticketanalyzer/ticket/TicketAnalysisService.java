package com.mcapps.ticketanalyzer.ticket;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class TicketAnalysisService {

    private final ChatClient chatClient;

    public TicketAnalysisService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public TicketAnalysis analyze(String description) {
        return chatClient
            .prompt()
            .system("""
                You are an experienced software engineering
                support analyst.
                
                Your task is to analyze software bug reports
                and classify them.
                
                Follow these rules:
                - Be concise and factual.
                - Do not invent information that is not present
                  in the ticket description.
                - If information is missing, list it in
                  missingInformation.
                - Use only the allowed enum values.
                - If you cannot determine the suggested team,
                  use UNKNOWN.
                - Distinguish between severity and priority.
                - Severity describes the technical impact
                  of the problem.
                """)
            .user(user -> user
                .text("""
                    Analyze the following software ticket:
                    {description}
                    """)
                .param("description", description))
            .call()
            .entity(TicketAnalysis.class,
                ChatClient.EntityParamSpec::validateSchema);
    }
}
