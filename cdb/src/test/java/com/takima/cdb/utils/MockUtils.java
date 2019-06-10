package com.takima.cdb.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.takima.cdb.entities.Company;
import com.takima.cdb.entities.Computer;

public class MockUtils {

	/// COMPANY
	
	/**
	 * Generate a valid Company instance.
	 * @param id .
	 * @return company.
	 */
	public static Company getValidCompany(Long id) {
		Company company = new Company();
		company.setId(id);
		company.setName("SUPER COMPANY");
		return company;
	}

	/**
	 * Generate an invalid Company instance.
	 * @param id .
	 * @return company.
	 */
	public static Company getInvalidCompany() {
		Company company = new Company();
		company.setId(null);
		company.setName(null);
		return company;
	}
	
	/**
	 * Returns a list of computers.
	 * @param size .
	 */
	public static List<Company> getCompanyList(int size){
		
		List<Company> companies = new ArrayList<>();
		Long id = (long) Math.random()*100;
		for(int i = 0; i < size; i++) {
			Company company = new Company();
			company.setId(id);
			
			id += (long) Math.random()*100;
			
			company.setName("POMME "+i);
			
			companies.add(company);
		}
		return companies;
	}
	
	/// COMPUTER
	
	/**
	 * Generate a valid Computer instance.
	 * @param id .
	 * @return computer.
	 */
	public static Computer getValidComputer(Long id) {
		Computer computer = new Computer();
		computer.setId(id);
		computer.setName("SUPER COMPUTER");
		computer.setIntroducedDate(LocalDate.of(2018, 12, 1));
		computer.setDiscontinuedDate(LocalDate.of(2018, 12, 2));
		computer.setManufacturer(1L);
		return computer;
	}

	/**
	 * Generate an invalid Computer instance, with invalid dates.
	 * @param id .
	 * @param name .
	 * @return computer.
	 */
	public static Computer getInvalidComputer(Long id) {
		Computer computer = new Computer();
		computer.setId(id);
		computer.setName("SUPER COMPUTER");
		computer.setIntroducedDate(LocalDate.of(2018, 12, 10));
		computer.setDiscontinuedDate(LocalDate.of(2018, 12, 2));
		computer.setManufacturer(1L);
		return computer;
	}
	
	/**
	 * Generate an invalid Computer instance, with valid dates and invalid name.
	 * @param id .
	 * @param name .
	 * @return computer.
	 */
	public static Computer getInvalidComputerWithValidDates(Long id) {
		Computer computer = new Computer();
		computer.setId(id);
		computer.setName("             ");
		computer.setIntroducedDate(LocalDate.of(2018, 12, 10));
		computer.setDiscontinuedDate(LocalDate.of(2018, 12, 20));
		computer.setManufacturer(1L);
		return computer;
	}
	
	/**
	 * Returns a list of computers.
	 * @param size .
	 */
	public static List<Computer> getComputerList(int size){
		
		List<Computer> computers = new ArrayList<>();
		Long id = (long) Math.random()*100;
		for(int i = 0; i < size; i++) {
			Computer computer = new Computer();
			computer.setId(id);
			
			id += (long) Math.random()*100;
			
			computer.setName("Macbooouuuk Praux "+i);
			
			Long timestamp1 = (long) (Math.random() * 1000000000);
			Long timestamp2 = timestamp1 + ((long) (Math.random() * 1000000));

			computer.setIntroducedDate(LocalDate.ofEpochDay(timestamp1));
			computer.setDiscontinuedDate(LocalDate.ofEpochDay(timestamp2));
			computer.setManufacturer(((long) Math.random()*21));
			
			computers.add(computer);
		}
		return computers;
	}

	/**
	 * Get the response as Map of String, Object.
	 * Stop the test if response's json is invalid.
	 * @param <T> .
	 * @param json Response.
	 * @return Map.
	 * @throws RuntimeException when objectmapper gets invalid json.
	 */
	public static <T> T getResponseAsMap(String json) {
	   	 try {
			 return new ObjectMapper().readValue(json, new TypeReference<T>() {});
		 } catch (Exception e) {
			 throw new RuntimeException(e);
		 }
	}
	
	
}
