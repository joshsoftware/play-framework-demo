package com.ranbhr.sample.controllers.apis.authentication;

public class AuthenticationException extends RuntimeException {
	
	public AuthenticationException(String message) {
		super(message);
	}
	
	public AuthenticationException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
