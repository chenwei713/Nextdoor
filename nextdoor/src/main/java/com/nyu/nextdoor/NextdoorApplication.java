package com.nyu.nextdoor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nyu.nextdoor.mapper")
public class NextdoorApplication {

	public static void main(String[] args) {
		SpringApplication.run(NextdoorApplication.class, args);
	}

}
