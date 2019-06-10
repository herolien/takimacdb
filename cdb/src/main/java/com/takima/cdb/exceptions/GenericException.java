package com.takima.cdb.exceptions;

import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.takima.cdb.utils.Errors;

/**
 * Abstract exception.
 */
public abstract class GenericException extends Exception {

	/** serialVersionUID */
	private static final long serialVersionUID = -3239171077863709797L;
	
	/** Http status code */
	private int httpStatus;
	
	/** Code of the current error */
	private String code;
	
	/** Message of the current error */
	private String message;
	
	/**
	 * Parameterized constructor 
	 * @param httpStatus The http status code.
	 * @param code The error code.
	 * @param message The error message. 
	 */
	public GenericException(int httpStatus, String code, String message) {
		super();
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}
	
	/**
	 * @return the httpStatus
	 */
	public int getHttpStatus() {
		return httpStatus;
	}

	/**
	 * @param httpStatus the httpStatus to set
	 */
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Get the exception as Map of String, String.
	 * @return a map of error code and error message.
	 */
	public Map<String, String> toMap(){
		return toMap(code, message);
	}
	
	/**
	 * Build a response.
	 * @return Response.
	 */
	public Response toResponse() {
		return Response.status(httpStatus)
					   .entity(this.toMap())
					   .type(MediaType.APPLICATION_JSON)
					   .build();
	}
	
	/**
	 * Get the exception as Map of String, String.
	 * @param code Error code.
	 * @param message Error message.
	 * @return a map of error code and error message.
	 */
	private static Map<String, String> toMap(String code, String message){
		Map<String, String> exception = new TreeMap<>();
		exception.put("code", code);
		exception.put("message", message);
		return exception;
	}
	
	/**
	 * Build a response.
	 * @param error .
	 * @return Response.
	 */
	public static Response toResponse(Errors error) {
		return Response.status(error.getStatus())
					   .entity(toMap(error.getCode(), error.getMessage()))
					   .type(MediaType.APPLICATION_JSON)
					   .build();
	}

}
