package com.takima.cdb.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common methods.
 */
public abstract class GenericController {

	/** ID attribute */
	public static final String PARAMETER_ID = "id";
	
	/** Logger */
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Log errors.
	 * @param methodName Name of the current method.
	 * @param exception The exception to log.
	 */
	protected void logError(String methodName, Exception exception) {
		logger.error(String.format("%s() webservice catch %s: %s", methodName, exception.getClass().getSimpleName(), exception.getMessage()), exception);
	}
	
	/**
	 * Check if the String contains numbers only (no sign nor space/characters).
	 * @param str The String to test.
	 * @return true if the string matches, false in other cases.
	 */
	public Boolean isNumeric(String str){
		return str.matches("[0-9]+");
	}
}
