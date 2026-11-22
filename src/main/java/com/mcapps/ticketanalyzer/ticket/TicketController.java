
package com.mcapps.ticketanalyzer.ticket;

import jakarta.validation.Valid;
import com.mcapps.ticketanalyzer.ticket.dto.CreateTicketRequest;
import com.mcapps.ticketanalyzer.ticket.dto.TicketResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(
        @Valid @RequestBody CreateTicketRequest request
    ) {
        TicketResponse response = ticketService.create(request);

        URI location = URI.create("/api/tickets/" + response.id());

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public TicketResponse findById(@PathVariable Long id) {
        return ticketService.findById(id);
    }

    @GetMapping
    public List<TicketResponse> findAll() {
        return ticketService.findAll();
    }
}
