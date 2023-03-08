package com.nikki.jwt;

import com.nikki.jwt.security.properties.DataSourceProperties;
import com.nikki.jwt.security.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ DataSourceProperties.class, JwtProperties.class})
public class JwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

}
