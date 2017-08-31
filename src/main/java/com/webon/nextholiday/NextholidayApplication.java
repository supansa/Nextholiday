package com.webon.nextholiday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.annotations.Cacheable;

@SpringBootApplication
@EnableCaching
public class NextholidayApplication {

	public static void main(String[] args) {
		SpringApplication.run(NextholidayApplication.class, args);
	}
}
