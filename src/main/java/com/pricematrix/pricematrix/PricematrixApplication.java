package com.pricematrix.pricematrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.pricematrix.pricematrix.pdf.CompanyProperties;

@SpringBootApplication
@EnableConfigurationProperties(CompanyProperties.class)
public class PricematrixApplication {

	public static void main(String[] args) {
		SpringApplication.run(PricematrixApplication.class, args);
	}

}