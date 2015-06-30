/**
 * 
 */
package com.ideamoment.ideajdbc4eclipse.table;

/**
 * @author Chinakite
 *
 */
public class InvalidValueException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public InvalidValueException() {
		super();
	}

	public InvalidValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidValueException(String message) {
		super(message);
	}

	public InvalidValueException(Throwable cause) {
		super(cause);
	}
}
