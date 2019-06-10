/**
 * 
 */
package com.takima.cdb.exceptions.mapper;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.takima.cdb.exceptions.GenericException;
import com.takima.cdb.utils.Errors;

/**
 * Catch an exception and return a particular json for this case.
 */
@Provider
public class UncaughtExceptionMapper implements ExceptionMapper<Throwable> {

	/** Logger */
	private final Logger log;
	
	/**
	 * Constructor
	 */
	public UncaughtExceptionMapper() {
		log = LoggerFactory.getLogger(this.getClass());
	}
	
	/**
	 * Generate a http status 500 response due to an unexpected exception.
	 * @param exception .
	 */
	@Override
	public Response toResponse(Throwable exception) {
		
		Response response;
		
		// Handle calls for non-existing webservices
		if(exception instanceof NotFoundException) {
			log.error("Invalid URL", exception);
			response = GenericException.toResponse(Errors.RESOURCE_NOT_FOUND);

		// Handle calls for non-supported content (eg. application/xml instead of application/json..).
		} else if(exception instanceof NotAllowedException) {
			log.error("Unsupported request method", exception);
			response = GenericException.toResponse(Errors.METHOD_NOT_ALLOWED);
				
		// Handle calls for non-existing webservices methods.
		} else if(exception instanceof NotSupportedException) {
			log.error("Unsupported media type", exception);
			response = GenericException.toResponse(Errors.UNSUPPORTED_MEDIA_TYPE);
				
		// Other cases
		} else {
			log.error("Unexpected exception", exception);
			response = GenericException.toResponse(Errors.UNEXPECTED_EXCEPTION);
		}
		
		return response;
	}
	

}
