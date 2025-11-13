package com.example.absoluteweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AbsoluteWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbsoluteWebApplication.class, args);

	}

}
