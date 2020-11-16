package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.example.demo.service.EventHelper;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.demo")
public class HuExAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(HuExAssignmentApplication.class, args);
		EventHelper eh = new EventHelper();
		eh.readExcel();
		
	}

}
