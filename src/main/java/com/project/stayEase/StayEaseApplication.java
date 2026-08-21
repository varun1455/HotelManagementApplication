package com.project.stayEase;

import com.project.stayEase.configuration.properties.DefaultPricingProperties;
import com.project.stayEase.configuration.properties.SeedProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({SeedProperties.class, DefaultPricingProperties.class})
public class StayEaseApplication {

	public static void main(String[] args) {
		java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("Asia/Kolkata"));
		SpringApplication.run(StayEaseApplication.class, args);
	}

}
