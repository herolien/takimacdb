/**
 * 
 */
package com.takima.cdb.exceptions.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.takima.cdb.exceptions.GenericException;
import com.takima.cdb.utils.Errors;

/**
 * Catch an exception and return a particular json for this case.
 */
@Provider
public class JsonExceptionMapper implements ExceptionMapper<JsonProcessingException> {

	/** Logger */
	private final Logger log;
	
	/**
	 * Constructor
	 */
	public JsonExceptionMapper() {
		log = LoggerFactory.getLogger(this.getClass());
	}
	
	/**
	 * Generate a http status 400 response due to an invalid body content format. 
	 * In other cases, generate a http status 500.
	 * @param exception .
	 */
	@Override
	public Response toResponse(JsonProcessingException exception) {

		Response response;
		
		// JsonParseException = Http status 400, invalid body content.
		if(exception instanceof JsonParseException) {
			log.error("Invalid body content", exception);
			response = GenericException.toResponse(Errors.BAD_REQUEST); 
		
		// JsonMappingException = Http status 400, invalid data set in attributes
		} else if(exception instanceof JsonMappingException) {
			log.error("Invalid attribute in body content", exception);
			response = GenericException.toResponse(Errors.INVALID_ENTITY); 
			
		// Other cases (eg. exception while formatting object to json...)
		} else {
			log.error("Unexpected exception", exception);
			response = GenericException.toResponse(Errors.UNEXPECTED_JSON_EXCEPTION); 
		}
		
		return response;
	}
	

}
