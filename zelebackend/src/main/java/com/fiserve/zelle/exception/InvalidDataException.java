package com.fiserve.zelle.exception;

public class InvalidDataException extends RuntimeException {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2492554577389979586L;

	public InvalidDataException() {
		super();
		
	}

	public InvalidDataException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public InvalidDataException(String message) {
		super(message);
		
	}
	

}
