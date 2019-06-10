/**
 * 
 */
package com.takima.cdb.exceptions;

import com.takima.cdb.utils.Errors;

/**
 * Exception to throw when database returns no result.
 */
public class EntityNotFoundException extends GenericException {

	/** serialVersionUID */
	private static final long serialVersionUID = 4715121680650983459L;

	/**
	 * Parameterized constructor.
	 * @param error The error.
	 */
	public EntityNotFoundException(Errors error) {
		super(error.getStatus(), error.getCode(), error.getMessage());
	}
	

}
