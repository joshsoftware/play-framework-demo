package com.ranbhr.sample.controllers.apis.authentication;

public class AuthException extends RuntimeException {
	
	private static String message = "Invalid credentials"; 
	
	public AuthException() {
		this(message);
	}
	
	public AuthException(String message) {
		super(message);
	}
	
	public AuthException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
