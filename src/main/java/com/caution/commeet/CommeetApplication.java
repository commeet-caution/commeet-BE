package com.caution.commeet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //BaseTimeEntity for EnableJpaListener
public class CommeetApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommeetApplication.class, args);
	}

}
