package com.mcapps.ticketanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiTicketAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiTicketAnalyzerApplication.class, args);
	}

}
