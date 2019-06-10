package com.takima.cdb;

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.takima.cdb.controllers.CompanyController;
import com.takima.cdb.controllers.ComputerController;
import com.takima.cdb.exceptions.mapper.JsonExceptionMapper;
import com.takima.cdb.exceptions.mapper.UncaughtExceptionMapper;

@SpringBootApplication
public class CdbApplication {
	@Bean
	public ResourceConfig resourceConfig() {
	    return new ResourceConfig().register(ComputerController.class)
	    						   .register(CompanyController.class)
	    						   .register(JacksonJaxbJsonProvider.class)  // hack to use in order to implement a custom JsonParseExceptionMapper
	    						   .register(JsonExceptionMapper.class)		 // Handle jackson exceptions during Jersey treatments
	    						   .register(UncaughtExceptionMapper.class)  // Handle unchecked exceptions
	    						   .register(CORSFilter.class);				 // Define Cross-origin rules 
	}
	  
	public static void main(String[] args) {
		SpringApplication.run(CdbApplication.class, args);
	}

}
