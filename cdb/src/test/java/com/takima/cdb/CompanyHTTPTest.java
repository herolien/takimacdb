/**
 * 
 */
package com.takima.cdb;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.takima.cdb.dao.Dao;
import com.takima.cdb.entities.Company;
import com.takima.cdb.utils.Errors;
import com.takima.cdb.utils.MockUtils;
import com.takima.cdb.utils.Paths;

/**
 * Company http test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CompanyHTTPTest {

	private static final String CODE_ERR_KEY = "code";
    private static final String MESSAGE_ERR_KEY = "message";

	@Autowired
    private TestRestTemplate restTemplate;
    
    @LocalServerPort
    private int port;
    
    @MockBean
    private Dao<Company> company;

    @Test
    public void getOk() {
    	
    	Company companyMock = MockUtils.getValidCompany(1234L);
    	Mockito.when(company.get(Mockito.<Long>any(), Mockito.<Class<Company>>any())).thenReturn(companyMock);
    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/company/1234", String.class);
        Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());
        
        assertEquals("Bad http status", 200, entity.getStatusCodeValue());
        compareValues(companyMock, response);
        
    }
    
    @Test
    public void getError() {
    	
    	Mockito.when(company.get(Mockito.<Long>any(), Mockito.<Class<Company>>any())).thenReturn(null);
    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/company/1234", String.class);
        Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());
        
        assertEquals("Bad http status", Errors.ENTITY_NOT_FOUND.getStatus(), entity.getStatusCodeValue());
        assertEquals("Bad code", Errors.ENTITY_NOT_FOUND.getCode(), response.get(CODE_ERR_KEY));
		assertEquals("Bad message", Errors.ENTITY_NOT_FOUND.getMessage(), response.get(MESSAGE_ERR_KEY));
		        
    }

    @Test
    public void getListOk() {
    	
    	List<Company> companyMock = MockUtils.getCompanyList(20);
    	Mockito.when(company.get(Mockito.anyInt(), Mockito.anyInt(), Mockito.<Class<Company>>any())).thenReturn(companyMock);
    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity(Paths.LIST_COMPANY+String.format("?offset=%s&limit=%s", 10L, 20L), String.class);
        List<Map<String, Object>> response = MockUtils.getResponseAsMap(entity.getBody());

        assertEquals("Bad http status", 200, entity.getStatusCodeValue());
        for(int i = 0; i < response.size(); i++) {
        	compareValues(companyMock.get(i), response.get(i));
        }
    }

    @Test
    public void getListKo() {
    	
    	List<Company> companyMock = new ArrayList<>();
    	Mockito.when(company.get(Mockito.anyInt(), Mockito.anyInt(), Mockito.<Class<Company>>any())).thenReturn(companyMock);
    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity(Paths.LIST_COMPANY+String.format("?offset=%s&limit=%s", 10L, 20L), String.class);
        Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());

        assertEquals("Bad http status", Errors.ENTITY_NOT_FOUND.getStatus(), entity.getStatusCodeValue());
        assertEquals("Bad code", Errors.ENTITY_NOT_FOUND.getCode(), response.get(CODE_ERR_KEY));
		assertEquals("Bad message", Errors.ENTITY_NOT_FOUND.getMessage(), response.get(MESSAGE_ERR_KEY));
		        
    }

    @Test
    public void getListInvalidOffset() {
    	    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity(Paths.LIST_COMPANY+String.format("?offset=%s&limit=%s", "hello", "world"), String.class);
        Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());
        
        assertEquals("Bad http status", Errors.INVALID_OFFSET.getStatus(), entity.getStatusCodeValue());
        assertEquals("Bad code", Errors.INVALID_OFFSET.getCode(), response.get(CODE_ERR_KEY));
        assertEquals("Bad message", Errors.INVALID_OFFSET.getMessage(), response.get(MESSAGE_ERR_KEY));
    }

    @Test
    public void getListInvalidLimit() {
    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity(Paths.LIST_COMPANY+String.format("?offset=%s&limit=%s", "1", "world"), String.class);
        Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());
        
        assertEquals("Bad http status", Errors.INVALID_LIMIT.getStatus(), entity.getStatusCodeValue());
        assertEquals("Bad code", Errors.INVALID_LIMIT.getCode(), response.get(CODE_ERR_KEY));
        assertEquals("Bad message", Errors.INVALID_LIMIT.getMessage(), response.get(MESSAGE_ERR_KEY));
    }

    @Test
    public void addOk() {
    	Company companyMock = MockUtils.getValidCompany(null);
    	final Long generatedId = 1234L;
    	
    	Mockito.doAnswer(invocation -> {
			
			Object[] args = invocation.getArguments();
			((Company) args[0]).setId(generatedId);
			return null;
			
		}).when(company).add(Mockito.any(Company.class));
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<Company> request = new HttpEntity<Company>(companyMock, headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange(Paths.ADD_COMPANY, HttpMethod.PUT, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", HttpStatus.OK.value(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		assertEquals("Bad ID", generatedId.toString(), response.getBody().get("id"));
		Mockito.verify(company, Mockito.times(1)).add(Mockito.any(Company.class));
		
    }

    @Test
    public void addError() {
    	Company companyMock = MockUtils.getInvalidCompany();
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<Company> request = new HttpEntity<Company>(companyMock, headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange(Paths.ADD_COMPANY, HttpMethod.PUT, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", Errors.INVALID_ENTITY.getStatus(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		assertEquals("Bad code", Errors.INVALID_ENTITY.getCode(), response.getBody().get(CODE_ERR_KEY));
		assertEquals("Bad message", Errors.INVALID_ENTITY.getMessage(), response.getBody().get(MESSAGE_ERR_KEY));
		
    }

    @Test
    public void updateOk() {
    	Company companyMock = MockUtils.getValidCompany(1234L);
    	
    	Mockito.when(company.update(Mockito.<Long>any(), Mockito.<Class<Company>>any(), Mockito.any(Company.class))).thenReturn(true);
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<Company> request = new HttpEntity<Company>(companyMock, headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange(Paths.UPDATE_COMPANY, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", HttpStatus.NO_CONTENT.value(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		Mockito.verify(company, Mockito.times(1)).update(Mockito.<Long>any(), Mockito.<Class<Company>>any(), Mockito.any(Company.class));
		
    }

    @Test
    public void updateError() {
    	Company companyMock = MockUtils.getInvalidCompany();

    	Mockito.when(company.update(Mockito.<Long>any(), Mockito.<Class<Company>>any(), Mockito.any(Company.class))).thenReturn(true);
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<Company> request = new HttpEntity<Company>(companyMock, headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange(Paths.UPDATE_COMPANY, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", Errors.INVALID_ENTITY.getStatus(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		assertEquals("Bad code", Errors.INVALID_ENTITY.getCode(), response.getBody().get(CODE_ERR_KEY));
		assertEquals("Bad message", Errors.INVALID_ENTITY.getMessage(), response.getBody().get(MESSAGE_ERR_KEY));
		Mockito.verify(company, Mockito.times(0)).update(Mockito.<Long>any(), Mockito.<Class<Company>>any(), Mockito.any(Company.class));
		
    }
    
    @Test
    public void deleteOk() {
    	Company companyMock = MockUtils.getValidCompany(1234L);
    	
    	Mockito.when(company.delete(Mockito.<Long>any(), Mockito.<Class<Company>>any())).thenReturn(true);
    	
    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<Company> request = new HttpEntity<Company>(companyMock, headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange("/company/1234", HttpMethod.DELETE, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", HttpStatus.NO_CONTENT.value(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		Mockito.verify(company, Mockito.times(1)).delete(Mockito.<Long>any(), Mockito.<Class<Company>>any());
		
    }   
    
    @Test
    public void deleteError() {
    	Company companyMock = MockUtils.getValidCompany(1234L);
    	
    	Mockito.when(company.delete(Mockito.<Long>any(), Mockito.<Class<Company>>any())).thenReturn(false);
    	
    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<Company> request = new HttpEntity<Company>(companyMock, headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange("/company/1234", HttpMethod.DELETE, request, new ParameterizedTypeReference<Map<String, String>>() {});

    	assertEquals("Bad http status", Errors.ENTITY_NOT_FOUND.getStatus(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
    	assertEquals("Bad code", Errors.ENTITY_NOT_FOUND.getCode(), response.getBody().get(CODE_ERR_KEY));
    	assertEquals("Bad message", Errors.ENTITY_NOT_FOUND.getMessage(), response.getBody().get(MESSAGE_ERR_KEY));
		Mockito.verify(company, Mockito.times(1)).delete(Mockito.<Long>any(), Mockito.<Class<Company>>any());
		
    }

    @Test
    public void deleteErrorConstraint() {
    	
    	Mockito.when(company.delete(Mockito.<Long>any(), Mockito.<Class<Company>>any())).thenThrow(new DataIntegrityViolationException("hello world"));
    	
    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<String> request = new HttpEntity<String>("", headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange("/company/1234", HttpMethod.DELETE, request, new ParameterizedTypeReference<Map<String, String>>() {});

    	assertEquals("Bad http status", Errors.DATA_INTEGRITY.getStatus(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
    	assertEquals("Bad code", Errors.DATA_INTEGRITY.getCode(), response.getBody().get(CODE_ERR_KEY));
    	assertEquals("Bad message", Errors.DATA_INTEGRITY.getMessage(), response.getBody().get(MESSAGE_ERR_KEY));
		Mockito.verify(company, Mockito.times(1)).delete(Mockito.<Long>any(), Mockito.<Class<Company>>any());
		
    }

    @Test
    public void countOk() {
    	
    	final Long sizeToReturn = 12L;
    	Mockito.when(company.count(Mockito.<Class<Company>>any())).thenReturn(sizeToReturn);
    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity(Paths.COUNT_COMPANY, String.class);
        Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());
        
        assertEquals("Bad http status", 200, entity.getStatusCodeValue());
        assertEquals("Bad returned size", sizeToReturn.toString(), response.get("size").toString());
        
    }
    
    private void compareValues(Company c1, Map<String, Object> c2) {
        assertEquals("bad id", c1.getId().toString(), c2.get("id").toString());
        assertEquals("bad name", c1.getName(), c2.get("name"));
    }
}
