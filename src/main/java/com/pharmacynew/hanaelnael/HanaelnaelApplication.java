package com.pharmacynew.hanaelnael;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.pharmacynew.hanaelnael")
@SpringBootApplication
public class HanaelnaelApplication {
	public static void main(String[] args) {

		SpringApplication.run(HanaelnaelApplication.class, args);
	}
}
