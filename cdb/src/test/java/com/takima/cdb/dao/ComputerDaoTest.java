package com.takima.cdb.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.takima.cdb.entities.Computer;
import com.takima.cdb.utils.MockUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ComputerDaoTest {

	@Resource
	private GenericDao<Computer> computer;

	@Test
	public void daoGet() throws Exception {
		assertNotNull("Computer must not be null", computer.get(5L, Computer.class));
	}
	
	@Test
	public void daoGetEmpty() throws Exception {
		assertNull("Computer must be null", computer.get(-1L, Computer.class));
	}

	@Test
	public void daoList() throws Exception {
		final Collection<Computer> computers = computer.get(10, 20, Computer.class);
		assertNotNull("Computer's collection must not be null", computers);
		assertEquals("Bad expected size", 20L, computers.size());
		computers.stream().forEach(e -> {
			assertTrue("Wrong id", e.getId() > 10L);
		});
	}
	
	@Test
	public void daoListEmpty() throws Exception {
		final Collection<Computer> computers = computer.get(999999901, 20, Computer.class);
		assertNotNull("Computer's collection must not be null", computers);
		assertEquals("Bad expected size", 0L, computers.size());
	}
	
	@Test
	public void daoAdd() throws Exception {
		final Computer computerMock = MockUtils.getValidComputer(null);
		computer.add(computerMock);
		assertNotNull("Missing generated id", computerMock.getId());
	}
	
	@Test
	public void daoUpdate() throws Exception {
		final Computer computerMock = MockUtils.getValidComputer(3L);
		computer.update(computerMock);
	}

	@Test
	public void daoDelete() throws Exception {
		computer.delete(3L, Computer.class);
	}

	@Test
	public void daoDeleteUnknownId() throws Exception {
		assertFalse("Bad response", computer.delete(12345678L, Computer.class));
	}
	
	@Test
	public void daoCount() throws Exception {
		assertTrue("bad counter", computer.count(Computer.class) > 0L);
	}
	
}
