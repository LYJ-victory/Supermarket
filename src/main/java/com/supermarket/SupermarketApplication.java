package com.supermarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.supermarket.mapper")
@SpringBootApplication
public class SupermarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupermarketApplication.class, args);
	}

}
