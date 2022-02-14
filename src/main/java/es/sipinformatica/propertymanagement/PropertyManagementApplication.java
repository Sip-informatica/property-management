package es.sipinformatica.propertymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.Generated;

@EnableScheduling
@SpringBootApplication
public class PropertyManagementApplication {
	@Generated
	public static void main(String[] args) {
		SpringApplication.run(PropertyManagementApplication.class, args);
	}

}
