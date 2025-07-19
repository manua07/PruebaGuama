package com.transactions.guama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.transactions.guama")

@ComponentScan(basePackages = "com.transactions.guama")
public class GuamaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuamaApplication.class, args);
	}

}
