/**
 * 
 */
package com.takima.cdb.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takima.cdb.CdbApplication;
import com.takima.cdb.dao.Dao;
import com.takima.cdb.entities.Company;
import com.takima.cdb.exceptions.EntityNotFoundException;
import com.takima.cdb.services.impl.CompanyServiceImpl;
import com.takima.cdb.utils.Errors;
import com.takima.cdb.utils.MockUtils;

/**
 * @author Julien
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = CdbApplication.class)
public class CompanyServiceTest {

	@Autowired
	private CompanyService service;
	
	@Mock
	private Dao<Company> dao;
	
	@Before
	public void init() {
		((CompanyServiceImpl) service).setCompanyDao(dao);
	}

	@Test
	public void testAdd() {
		final Long generatedId = 1234L;
		final Company company = MockUtils.getValidCompany(null);
		
		Mockito.doAnswer(invocation -> {
			
			Object[] args = invocation.getArguments();
			((Company) args[0]).setId(generatedId);
			return null;
			
		}).when(dao).add(Mockito.any(Company.class));
		
		service.addCompany(company);
		
		assertNotNull("No ID set in company", company.getId());
		
	}

	@Test
	public void testUpdate() {
		final Company company = MockUtils.getValidCompany(null);
		
		Mockito.doNothing().when(dao).update(Mockito.any(Company.class));
		
		service.updateCompany(company);

		Mockito.verify(dao, Mockito.times(1)).update(Mockito.any(Company.class));
		
	}

	@Test
	public void testGet() {
		
		final Long id = 123L;
		
		Company company = MockUtils.getValidCompany(id);
		ObjectMapper parser = new ObjectMapper();
		
		Mockito.when(dao.get(Mockito.<Long>any(), Mockito.<Class<Company>>any()))
			   .thenReturn(company);
		
		try {
			Company company2 = service.getCompany(id);
			assertEquals("Bad json", parser.writeValueAsString(company), parser.writeValueAsString(company2));
			
		} catch (Exception e) {
			fail("Unexpected exception: "+e.getMessage());
		}
	}

	@Test
	public void testGetList() {
		
		final int size = 10;
		
		Collection<Company> companies = MockUtils.getCompanyList(size);
		ObjectMapper parser = new ObjectMapper();
		
		Mockito.when(dao.get(Mockito.anyInt(), Mockito.anyInt(), Mockito.<Class<Company>>any()))
			   .thenReturn(companies);
		
		try {
			Collection<Company> companies2 = service.getCompanies(0, size);
			assertEquals("Bad list size", size, companies.size());
			assertEquals("Bad json", parser.writeValueAsString(companies), parser.writeValueAsString(companies2));
			
		} catch (Exception e) {
			fail("Unexpected exception: "+e.getMessage());
		}
	}

	@Test
	public void testGetListError() {
		
		final int size = 10;
		
		Collection<Company> companies = Collections.emptyList();
		
		Mockito.when(dao.get(Mockito.anyInt(), Mockito.anyInt(), Mockito.<Class<Company>>any()))
			   .thenReturn(companies);
		
		try {
			service.getCompanies(0, size);
			fail();
		
		} catch (EntityNotFoundException e1) {
			assertEquals("bad message", Errors.ENTITY_NOT_FOUND.getMessage(), e1.getMessage());
			
		} catch (Exception e) {
			fail("Unexpected exception: "+e.getMessage());
		}
	}

	@Test
	public void testRemove() {
		final Company company = MockUtils.getValidCompany(null);
		
		Mockito.when(dao.delete(Mockito.<Long>any(), Mockito.<Class<Company>>any())).thenReturn(true);
		
		try {
			service.deleteCompany(company.getId());
			Mockito.verify(dao, Mockito.times(1)).delete(Mockito.<Long>any(), Mockito.<Class<Company>>any());
			
		} catch (Exception e) {
			fail("Unexpected exception: "+e.getMessage()); 
		}
		
	}
	
	@Test
	public void testRemoveFail() {
		final Company company = MockUtils.getValidCompany(null);
		
		Mockito.when(dao.delete(Mockito.<Long>any(), Mockito.<Class<Company>>any())).thenReturn(false);
		
		try {
			service.deleteCompany(company.getId());
			fail("Unexpected behavior");
			
		} catch (EntityNotFoundException e1) {
			Mockito.verify(dao, Mockito.times(1)).delete(Mockito.<Long>any(), Mockito.<Class<Company>>any());
			compareEntityNotFoundException(e1);
		
		} catch (Exception e2) {
			fail("Unexpected exception: "+e2.getMessage()); 
		}
		
	}
	
	private void compareEntityNotFoundException(EntityNotFoundException e1) {

		assertEquals("Bad error (code)", Errors.ENTITY_NOT_FOUND.getCode(), e1.getCode());
		assertEquals("Bad error (message)", Errors.ENTITY_NOT_FOUND.getMessage(), e1.getMessage());
		assertEquals("Bad error (status)", Errors.ENTITY_NOT_FOUND.getStatus(), e1.getHttpStatus());
	}
	
	
}
