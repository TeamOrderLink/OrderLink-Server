package com.order.orderlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
// @EnableJpaAuditing
public class OrderLinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderLinkApplication.class, args);
	}

}
