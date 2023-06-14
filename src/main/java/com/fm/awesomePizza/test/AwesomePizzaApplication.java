package com.fm.awesomePizza.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Pizza Awesome API", version = "2.0", description = "Pizza Awesome Orders"))
public class AwesomePizzaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwesomePizzaApplication.class, args);
	}
	

}
