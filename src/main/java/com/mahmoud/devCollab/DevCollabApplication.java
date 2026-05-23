package com.mahmoud.devCollab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DevCollabApplication {
	public static void main(String[] args) {
		SpringApplication.run(DevCollabApplication.class, args);
	}
}
