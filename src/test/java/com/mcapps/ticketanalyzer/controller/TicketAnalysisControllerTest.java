package com.mcapps.ticketanalyzer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mcapps.ticketanalyzer.ticket.SuggestedTeam;
import com.mcapps.ticketanalyzer.ticket.TicketAnalysis;
import com.mcapps.ticketanalyzer.ticket.TicketAnalysisRequest;
import com.mcapps.ticketanalyzer.ticket.TicketAnalysisService;
import com.mcapps.ticketanalyzer.ticket.TicketCategory;
import com.mcapps.ticketanalyzer.ticket.TicketSeverity;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(TicketAnalysisController.class)
class TicketAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TicketAnalysisService ticketAnalysisService;

    @Test
    void shouldReturnTicketAnalysis() throws Exception {

        TicketAnalysis analysis = new TicketAnalysis(
            "Profile cannot be saved",
            TicketCategory.BUG,
            TicketSeverity.HIGH,
            "Saving a profile returns HTTP 500.",
            List.of("Application logs"),
            SuggestedTeam.BACKEND
        );

        when(ticketAnalysisService.analyze(any(String.class)))
            .thenReturn(analysis);

        TicketAnalysisRequest request = new TicketAnalysisRequest(
            "Saving a profile returns HTTP 500."
        );

        mockMvc.perform(post("/api/tickets/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title")
                .value("Profile cannot be saved"))
            .andExpect(jsonPath("$.category").value("BUG"))
            .andExpect(jsonPath("$.severity").value("HIGH"))
            .andExpect(jsonPath("$.suggestedTeam").value("BACKEND"));

        verify(ticketAnalysisService).analyze(
            "Saving a profile returns HTTP 500."
        );
    }

    @Test
    void shouldRejectBlankDescription() throws Exception {

        mockMvc.perform(post("/api/tickets/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "description": "   "
                    }
                    """))
            .andExpect(status().isBadRequest());

        verifyNoInteractions(ticketAnalysisService);
    }
}