package com.ranbhr.sample.controllers.apis.jwtauth;

public class AuthException extends RuntimeException {
	
	private static String message = "Unauthorized"; 
	
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
