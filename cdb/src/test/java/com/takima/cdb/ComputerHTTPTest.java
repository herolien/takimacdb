/**
 * 
 */
package com.takima.cdb;

import static org.junit.Assert.assertEquals;

import java.time.format.DateTimeFormatter;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.takima.cdb.dao.Dao;
import com.takima.cdb.entities.Computer;
import com.takima.cdb.utils.Errors;
import com.takima.cdb.utils.MockUtils;
import com.takima.cdb.utils.Paths;

/**
 * Computer http test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ComputerHTTPTest {

	private static final String CODE_ERR_KEY = "code";
    private static final String MESSAGE_ERR_KEY = "message";

	@Autowired
    private TestRestTemplate restTemplate;
    
    @LocalServerPort
    private int port;
    
    @MockBean
    private Dao<Computer> computer;

    @Test
    public void getOk() {
    	
    	Computer computerMock = MockUtils.getValidComputer(1234L);
    	Mockito.when(computer.get(Mockito.<Long>any(), Mockito.<Class<Computer>>any())).thenReturn(computerMock);
    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/computer/1234", String.class);
        Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());
        
        assertEquals("Bad http status", 200, entity.getStatusCodeValue());
        compareValues(computerMock, response);
        
    }
    
    @Test
    public void getError() {
 
    	Mockito.when(computer.get(Mockito.<Long>any(), Mockito.<Class<Computer>>any())).thenReturn(null);
    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/computer/1234", String.class);
        Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());
        
        assertEquals("Bad http status", Errors.ENTITY_NOT_FOUND.getStatus(), entity.getStatusCodeValue());
        assertEquals("Bad code", Errors.ENTITY_NOT_FOUND.getCode(), response.get(CODE_ERR_KEY));
		assertEquals("Bad message", Errors.ENTITY_NOT_FOUND.getMessage(), response.get(MESSAGE_ERR_KEY));
		        
    }

    @Test
    public void getListOk() {
    	
    	List<Computer> computerMock = MockUtils.getComputerList(20);
    	Mockito.when(computer.get(Mockito.anyInt(), Mockito.anyInt(), Mockito.<Class<Computer>>any())).thenReturn(computerMock);
    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity(Paths.LIST_COMPUTER+String.format("?offset=%s&limit=%s", 10L, 20L), String.class);
        List<Map<String, Object>> response = MockUtils.getResponseAsMap(entity.getBody());

        assertEquals("Bad http status", 200, entity.getStatusCodeValue());
        for(int i = 0; i < response.size(); i++) {
        	compareValues(computerMock.get(i), response.get(i));
        }
    }

    @Test
    public void getListKo() {
    	
    	List<Computer> computerMock = new ArrayList<>();
    	Mockito.when(computer.get(Mockito.anyInt(), Mockito.anyInt(), Mockito.<Class<Computer>>any())).thenReturn(computerMock);
    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity(Paths.LIST_COMPUTER+String.format("?offset=%s&limit=%s", 10L, 20L), String.class);
        Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());

        assertEquals("Bad http status", Errors.ENTITY_NOT_FOUND.getStatus(), entity.getStatusCodeValue());
        assertEquals("Bad code", Errors.ENTITY_NOT_FOUND.getCode(), response.get(CODE_ERR_KEY));
		assertEquals("Bad message", Errors.ENTITY_NOT_FOUND.getMessage(), response.get(MESSAGE_ERR_KEY));
		        
    }

    @Test
    public void getListInvalidOffset() {
    	    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity(Paths.LIST_COMPUTER+String.format("?offset=%s&limit=%s", "hello", "world"), String.class);
        Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());
        
        assertEquals("Bad http status", Errors.INVALID_OFFSET.getStatus(), entity.getStatusCodeValue());
        assertEquals("Bad code", Errors.INVALID_OFFSET.getCode(), response.get("code"));
        assertEquals("Bad message", Errors.INVALID_OFFSET.getMessage(), response.get("message"));
    }

    @Test
    public void getListInvalidLimit() {
    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity(Paths.LIST_COMPUTER+String.format("?offset=%s&limit=%s", "1", "world"), String.class);
        Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());
        
        assertEquals("Bad http status", Errors.INVALID_LIMIT.getStatus(), entity.getStatusCodeValue());
        assertEquals("Bad code", Errors.INVALID_LIMIT.getCode(), response.get("code"));
        assertEquals("Bad message", Errors.INVALID_LIMIT.getMessage(), response.get("message"));
    }

    @Test
    public void addOk() {
    	Computer computerMock = MockUtils.getValidComputer(null);
    	final Long generatedId = 1234L;
    	
    	Mockito.doAnswer(invocation -> {
			
			Object[] args = invocation.getArguments();
			((Computer) args[0]).setId(generatedId);
			return null;
			
		}).when(computer).add(Mockito.any(Computer.class));
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<Computer> request = new HttpEntity<Computer>(computerMock, headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange(Paths.ADD_COMPUTER, HttpMethod.PUT, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", HttpStatus.OK.value(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		assertEquals("Bad ID", generatedId.toString(), response.getBody().get("id"));
		Mockito.verify(computer, Mockito.times(1)).add(Mockito.any(Computer.class));
		
    }

    @Test
    public void addError() {
    	Computer computerMock = MockUtils.getInvalidComputer(null);
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<Computer> request = new HttpEntity<Computer>(computerMock, headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange(Paths.ADD_COMPUTER, HttpMethod.PUT, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", Errors.INVALID_ENTITY.getStatus(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		assertEquals("Bad code", Errors.INVALID_ENTITY.getCode(), response.getBody().get(CODE_ERR_KEY));
		assertEquals("Bad message", Errors.INVALID_ENTITY.getMessage(), response.getBody().get(MESSAGE_ERR_KEY));
		
    }

    @Test
    public void updateOk() {
    	Computer computerMock = MockUtils.getValidComputer(1234L);
    	
    	Mockito.doNothing().when(computer).update(Mockito.any(Computer.class));
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<Computer> request = new HttpEntity<Computer>(computerMock, headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange(Paths.UPDATE_COMPUTER, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", HttpStatus.NO_CONTENT.value(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		Mockito.verify(computer, Mockito.times(1)).update(Mockito.any(Computer.class));
		
    }

    @Test
    public void updateError() {
    	Computer computerMock = MockUtils.getInvalidComputer(1234L, "                    ");
    	
    	Mockito.doNothing().when(computer).update(Mockito.any(Computer.class));
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<Computer> request = new HttpEntity<Computer>(computerMock, headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange(Paths.UPDATE_COMPUTER, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", Errors.INVALID_ENTITY.getStatus(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		assertEquals("Bad code", Errors.INVALID_ENTITY.getCode(), response.getBody().get(CODE_ERR_KEY));
		assertEquals("Bad message", Errors.INVALID_ENTITY.getMessage(), response.getBody().get(MESSAGE_ERR_KEY));
		Mockito.verify(computer, Mockito.times(0)).update(Mockito.any(Computer.class));
		
    }
    
    @Test
    public void deleteOk() {
    	Computer computerMock = MockUtils.getValidComputer(1234L);
    	
    	Mockito.when(computer.delete(Mockito.<Long>any(), Mockito.<Class<Computer>>any())).thenReturn(true);
    	
    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<Computer> request = new HttpEntity<Computer>(computerMock, headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange("/computer/1234", HttpMethod.DELETE, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", HttpStatus.NO_CONTENT.value(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		Mockito.verify(computer, Mockito.times(1)).delete(Mockito.<Long>any(), Mockito.<Class<Computer>>any());
		
    }   
    
    @Test
    public void deleteError() {
    	Computer computerMock = MockUtils.getValidComputer(1234L);
    	
    	Mockito.when(computer.delete(Mockito.<Long>any(), Mockito.<Class<Computer>>any())).thenReturn(false);
    	
    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<Computer> request = new HttpEntity<Computer>(computerMock, headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange("/computer/1234", HttpMethod.DELETE, request, new ParameterizedTypeReference<Map<String, String>>() {});

    	assertEquals("Bad http status", Errors.ENTITY_NOT_FOUND.getStatus(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
    	assertEquals("Bad code", Errors.ENTITY_NOT_FOUND.getCode(), response.getBody().get(CODE_ERR_KEY));
    	assertEquals("Bad message", Errors.ENTITY_NOT_FOUND.getMessage(), response.getBody().get(MESSAGE_ERR_KEY));
		Mockito.verify(computer, Mockito.times(1)).delete(Mockito.<Long>any(), Mockito.<Class<Computer>>any());
		
    }

    @Test
    public void countOk() {
    	
    	final Long sizeToReturn = 12L;
    	Mockito.when(computer.count(Mockito.<Class<Computer>>any())).thenReturn(sizeToReturn);
    	
        ResponseEntity<String> entity = this.restTemplate.getForEntity(Paths.COUNT_COMPUTER, String.class);
        Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());
        
        assertEquals("Bad http status", 200, entity.getStatusCodeValue());
        assertEquals("Bad returned size", sizeToReturn.toString(), response.get("size").toString());
        
    }
    
    private void compareValues(Computer c1, Map<String, Object> c2) {
        assertEquals("bad id", c1.getId().toString(), c2.get("id").toString());
        assertEquals("bad name", c1.getName(), c2.get("name"));
        assertEquals("bad introduced date", c1.getIntroducedDate().format(DateTimeFormatter.ISO_DATE), c2.get("introducedDate"));
        assertEquals("bad discontinued date", c1.getDiscontinuedDate().format(DateTimeFormatter.ISO_DATE), c2.get("discontinuedDate"));
        assertEquals("bad manufacturer", c1.getManufacturer().toString(), c2.get("manufacturer").toString());
    }
}
