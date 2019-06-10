package com.takima.cdb.exceptions;

import com.takima.cdb.utils.Errors;

/**
 * Exception to throw when sent parameters are invalid.
 */
public class InvalidArgumentException extends GenericException {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 3638452063428754560L;

	/**
	 * Parameterized constructor.
	 * @param error The error.
	 */
	public InvalidArgumentException(Errors error) {
		super(error.getStatus(), error.getCode(), error.getMessage());
	}
	
}
