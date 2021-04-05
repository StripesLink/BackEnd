package edu.eci.escuelaing.StripesLink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("edu.eci.escuelaing")
@EnableMongoRepositories("edu.eci.escuelaing")
public class StripesLinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(StripesLinkApplication.class, args);
	}

}
