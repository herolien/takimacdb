package com.takima.cdb.utils;

/**
 * Errors constants.
 */
public enum Errors {
	
	///// Business errors
	
	/** No results found in database */
	ENTITY_NOT_FOUND(404, "ENTITY_404", "No results found"),
	
	/** Parameter "offset" is missing or invalid */
	INVALID_OFFSET(400, "OFFSET_400", "Parameter 'offset' is missing or invalid"),
	
	/** Parameter "limit" is missing or invalid */
	INVALID_LIMIT(400, "LIMIT_400", "Parameter 'limit' is missing or invalid"),
	
	/** One of entities' attributes is invalid */
	INVALID_ENTITY(400, "ENTITY_400", "Invalid attribute(s) among sent data"),
	
	/** Data integrity error */
	DATA_INTEGRITY(400, "INTEGRITY_400", "Cannot perform the action due to constraints between entities."),
	
	///// Technical errors
	
	/** Bad request - Invalid body content (400) */
	BAD_REQUEST(400, "PATH_400", "Bad request"),
	
	/** Invalid URL (404) */
	RESOURCE_NOT_FOUND(404, "PATH_404", "Resource not found"),
	
	/** Unsupported method (405) */
	METHOD_NOT_ALLOWED(405, "PATH_405", "Method not allowed"),
	
	/** Unsupported media type (415) */
	UNSUPPORTED_MEDIA_TYPE(415, "PATH_415", "Unsupported media type"),
	
	/** Parse/Format JSON unexpected exception (500) */
	UNEXPECTED_JSON_EXCEPTION(500, "JSON_500", "Unexpected error during processing of supplied data"),
	
	/** Unexpected exception (500) */
	UNEXPECTED_EXCEPTION(500, "PATH_500", "Unexpected error");
	
	
	/** Http status */
	private int status;
	
	/** Error code */
	private String code;
	
	/** Error message */
	private String message;
	
	/**
	 * Constructor.
	 * @param status Http status code.
	 * @param code Error code.
	 * @param message Error message.
	 */
	private Errors(int status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
}
