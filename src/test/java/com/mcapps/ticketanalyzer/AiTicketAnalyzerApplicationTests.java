package com.mcapps.ticketanalyzer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "app.ticket-analysis.fixed-delay=1h")
class AiTicketAnalyzerApplicationTests {

	@Test
	void contextLoads() {
	}

}
