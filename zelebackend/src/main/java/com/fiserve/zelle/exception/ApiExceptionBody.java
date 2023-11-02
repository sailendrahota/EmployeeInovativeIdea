package com.fiserve.zelle.exception;

public class ApiExceptionBody {

	public String type;
	public String message;
	
	
	public ApiExceptionBody(String type, String message) {
		this.type = type;
		this.message = message;
	}
	/**
	public ApiExceptionBody(String type, String message, Object data) {
		this.type = type;
		this.message = message;
		this.data = data;
	}
	*/


	public ApiExceptionBody() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	
	
	
}
