package com.takima.cdb.exceptions.mapper;

import static org.junit.Assert.assertEquals;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.takima.cdb.services.ComputerService;
import com.takima.cdb.utils.Errors;
import com.takima.cdb.utils.MockUtils;
import com.takima.cdb.utils.Paths;

/**
 * Test webservice mappers.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MapperHTTPTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	    
	@LocalServerPort
	private int port;

    @MockBean
    private ComputerService computer;
    
    /**
     * Test a 400 error due to a bad request (eg. bad json format).
     */
    @Test
    public void errorBadRequest() {
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<String> request = new HttpEntity<String>("{\"hello world\" : 'hey yo'}", headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange(Paths.ADD_COMPUTER, HttpMethod.PUT, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", Errors.BAD_REQUEST.getStatus(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		assertEquals("Bad code", Errors.BAD_REQUEST.getCode(), response.getBody().get("code"));
		assertEquals("Bad message", Errors.BAD_REQUEST.getMessage(), response.getBody().get("message"));
    }
    
    /**
     * Test a 400 error due to invalid data set in entity.
     */
    @Test
    public void errorInvalidData() {
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<String> request = new HttpEntity<String>("{\"id\" : \"hey yo\"}", headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange(Paths.ADD_COMPUTER, HttpMethod.PUT, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", Errors.INVALID_ENTITY.getStatus(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		assertEquals("Bad code", Errors.INVALID_ENTITY.getCode(), response.getBody().get("code"));
		assertEquals("Bad message", Errors.INVALID_ENTITY.getMessage(), response.getBody().get("message"));
    }
    
    /**
     * Test a 404 error due to a wrong path.
     */
    @Test
    public void errorInvalidPath() {
    	 ResponseEntity<String> entity = this.restTemplate.getForEntity("/ma-superbe-voiture/12345", String.class);
    	 Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());

    	 assertEquals("Bad http status", Errors.RESOURCE_NOT_FOUND.getStatus(), entity.getStatusCodeValue());
		 assertEquals("Bad code", Errors.RESOURCE_NOT_FOUND.getCode(), response.get("code"));
		 assertEquals("Bad message", Errors.RESOURCE_NOT_FOUND.getMessage(), response.get("message"));
    }

    /**
     * Test a 405 error due to a wrong method on a certain path.
     */
    @Test
    public void errorMethodNotAllowed() {
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_RSS_XML);
    	HttpEntity<String> request = new HttpEntity<String>("", headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange("/computer/add", HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", Errors.METHOD_NOT_ALLOWED.getStatus(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		assertEquals("Bad code", Errors.METHOD_NOT_ALLOWED.getCode(), response.getBody().get("code"));
		assertEquals("Bad message", Errors.METHOD_NOT_ALLOWED.getMessage(), response.getBody().get("message"));
    }
    
    /**
     * Test a 415 error due to a wrong content type declaration.
     */
    @Test
    public void errorUnsupportedMediaType() {
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_RSS_XML);
    	HttpEntity<String> request = new HttpEntity<String>("", headers);
    	HttpEntity<Map<String, String>> response = this.restTemplate.exchange("/computer/add", HttpMethod.PUT, request, new ParameterizedTypeReference<Map<String, String>>() {});
    	
    	assertEquals("Bad http status", Errors.UNSUPPORTED_MEDIA_TYPE.getStatus(), ((ResponseEntity<Map<String, String>>) response).getStatusCodeValue());
		assertEquals("Bad code", Errors.UNSUPPORTED_MEDIA_TYPE.getCode(), response.getBody().get("code"));
		assertEquals("Bad message", Errors.UNSUPPORTED_MEDIA_TYPE.getMessage(), response.getBody().get("message"));
    }

    /**
     * Test a 500 error.
     */
    @Test
    public void errorInternalServerErr() throws Exception {
    	
    	Mockito.when(computer.getComputer(Mockito.anyLong())).thenThrow(new RuntimeException("Oops..... UNEXPECTED EXCEPTION"));
    	
    	ResponseEntity<String> entity = this.restTemplate.getForEntity("/computer/12345678901345678", String.class);
    	Map<String, Object> response = MockUtils.getResponseAsMap(entity.getBody());
    	
    	assertEquals("Bad http status", Errors.UNEXPECTED_EXCEPTION.getStatus(), entity.getStatusCodeValue());
    	assertEquals("Bad code", Errors.UNEXPECTED_EXCEPTION.getCode(), response.get("code"));
    	assertEquals("Bad message", Errors.UNEXPECTED_EXCEPTION.getMessage(), response.get("message"));
    }
    
}
