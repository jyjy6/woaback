package jy.WorkOutwithAgent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class WorkOutwithAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkOutwithAgentApplication.class, args);

		log.info("Hello! WorkOutwithAgent Activated!");
	}

}
