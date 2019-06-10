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

import com.takima.cdb.entities.Company;
import com.takima.cdb.utils.MockUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CompanyDaoTest {

	@Resource
	private GenericDao<Company> company;

	@Test
	public void daoGet() throws Exception {
		assertNotNull("Company must not be null", company.get(5L, Company.class));
	}
	
	@Test
	public void daoGetEmpty() throws Exception {
		assertNull("Company must be null", company.get(-1L, Company.class));
	}

	@Test
	public void daoList() throws Exception {
		final Collection<Company> companies = company.get(1, 12, Company.class);
		assertNotNull("Company's collection must not be null", companies);
		assertEquals("Bad expected size", 12, companies.size());
		companies.stream().forEach(e -> {
			assertTrue("Wrong id", e.getId() > 1L);
		});
	}
	
	@Test
	public void daoListEmpty() throws Exception {
		final Collection<Company> companies = company.get(999999901, 20, Company.class);
		assertNotNull("Company's collection must not be null", companies);
		assertEquals("Bad expected size", 0L, companies.size());
	}
	
	@Test
	public void daoAdd() throws Exception {
		final Company companyMock = MockUtils.getValidCompany(null);
		company.add(companyMock);
		assertNotNull("Missing generated id", companyMock.getId());
	}
	
	@Test
	public void daoUpdate() throws Exception {
		final Company companyMock = MockUtils.getValidCompany(3L);
		company.update(companyMock);
	}

	@Test
	public void daoDelete() throws Exception {
		company.delete(3L, Company.class);
	}

	@Test
	public void daoDeleteUnknownId() throws Exception {
		assertFalse("Bad response", company.delete(12345678L, Company.class));
	}
	
	@Test
	public void daoCount() throws Exception {
		assertTrue("bad counter", company.count(Company.class) > 0L);
	}
	
}
