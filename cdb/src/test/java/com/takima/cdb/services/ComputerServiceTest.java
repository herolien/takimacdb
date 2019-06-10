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
import com.takima.cdb.entities.Computer;
import com.takima.cdb.exceptions.EntityNotFoundException;
import com.takima.cdb.services.impl.ComputerServiceImpl;
import com.takima.cdb.utils.Errors;
import com.takima.cdb.utils.MockUtils;

/**
 * @author Julien
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = CdbApplication.class)
public class ComputerServiceTest {

	@Autowired
	private ComputerService service;
	
	@Mock
	private Dao<Computer> dao;
	
	@Before
	public void init() {
		((ComputerServiceImpl) service).setComputerDao(dao);
	}

	@Test
	public void testAdd() {
		final Long generatedId = 1234L;
		final Computer computer = MockUtils.getValidComputer(null);
		
		Mockito.doAnswer(invocation -> {
			
			Object[] args = invocation.getArguments();
			((Computer) args[0]).setId(generatedId);
			return null;
			
		}).when(dao).add(Mockito.any(Computer.class));
		
		service.addComputer(computer);
		
		assertNotNull("No ID set in computer", computer.getId());
		
	}

	@Test
	public void testUpdate() throws Exception {
		final Computer computer = MockUtils.getValidComputer(1234L);
		
		Mockito.when(dao.update(Mockito.<Long>any(), Mockito.<Class<Computer>>any(), Mockito.any(Computer.class))).thenReturn(true);
		
		service.updateComputer(computer);

		Mockito.verify(dao, Mockito.times(1)).update(Mockito.<Long>any(), Mockito.<Class<Computer>>any(), Mockito.any(Computer.class));
		
	}

	@Test
	public void testUpdateFail() throws Exception {
		final Computer computer = MockUtils.getValidComputer(1234L);
		
		Mockito.when(dao.update(Mockito.<Long>any(), Mockito.<Class<Computer>>any(), Mockito.any(Computer.class))).thenReturn(false);

		try {
			service.updateComputer(computer);
			fail();
		
		} catch (EntityNotFoundException e1) {
			assertEquals("bad message", Errors.ENTITY_NOT_FOUND.getMessage(), e1.getMessage());
			
		} catch (Exception e) {
			fail("Unexpected exception: "+e.getMessage());
		}
		Mockito.verify(dao, Mockito.times(1)).update(Mockito.<Long>any(), Mockito.<Class<Computer>>any(), Mockito.any(Computer.class));
		
	}

	@Test
	public void testGet() {
		
		final Long id = 123L;
		
		Computer computer = MockUtils.getValidComputer(id);
		ObjectMapper parser = new ObjectMapper();
		
		Mockito.when(dao.get(Mockito.<Long>any(), Mockito.<Class<Computer>>any()))
			   .thenReturn(computer);
		
		try {
			Computer computer2 = service.getComputer(id);
			assertEquals("Bad json", parser.writeValueAsString(computer), parser.writeValueAsString(computer2));
			
		} catch (Exception e) {
			fail("Unexpected exception: "+e.getMessage());
		}
	}

	@Test
	public void testGetList() {
		
		final int size = 10;
		
		Collection<Computer> computers = MockUtils.getComputerList(size);
		ObjectMapper parser = new ObjectMapper();
		
		Mockito.when(dao.get(Mockito.anyInt(), Mockito.anyInt(), Mockito.<Class<Computer>>any()))
			   .thenReturn(computers);
		
		try {
			Collection<Computer> computers2 = service.getComputers(0, size);
			assertEquals("Bad list size", size, computers.size());
			assertEquals("Bad json", parser.writeValueAsString(computers), parser.writeValueAsString(computers2));
			
		} catch (Exception e) {
			fail("Unexpected exception: "+e.getMessage());
		}
	}
	
	@Test
	public void testGetListError() {
		
		final int size = 10;
		
		Collection<Computer> computers = Collections.emptyList();
		
		Mockito.when(dao.get(Mockito.anyInt(), Mockito.anyInt(), Mockito.<Class<Computer>>any()))
			   .thenReturn(computers);
		
		try {
			service.getComputers(0, size);
			fail();
		
		} catch (EntityNotFoundException e1) {
			assertEquals("bad message", Errors.ENTITY_NOT_FOUND.getMessage(), e1.getMessage());
			
		} catch (Exception e) {
			fail("Unexpected exception: "+e.getMessage());
		}
	}

	@Test
	public void testRemove() {
		final Computer computer = MockUtils.getValidComputer(null);
		
		Mockito.when(dao.delete(Mockito.<Long>any(), Mockito.<Class<Computer>>any())).thenReturn(true);
		
		try {
			service.deleteComputer(computer.getId());
			Mockito.verify(dao, Mockito.times(1)).delete(Mockito.<Long>any(), Mockito.<Class<Computer>>any());
			
		} catch (Exception e) {
			fail("Unexpected exception: "+e.getMessage()); 
		}
		
	}
	
	@Test
	public void testRemoveFail() {
		final Computer computer = MockUtils.getValidComputer(null);
		
		Mockito.when(dao.delete(Mockito.<Long>any(), Mockito.<Class<Computer>>any())).thenReturn(false);
		
		try {
			service.deleteComputer(computer.getId());
			fail("Unexpected behavior");
			
		} catch (EntityNotFoundException e1) {
			Mockito.verify(dao, Mockito.times(1)).delete(Mockito.<Long>any(), Mockito.<Class<Computer>>any());
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
