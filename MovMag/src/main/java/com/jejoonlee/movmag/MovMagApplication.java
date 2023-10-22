package com.jejoonlee.movmag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MovMagApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovMagApplication.class, args);
	}

}
