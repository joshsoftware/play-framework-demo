package com.ranbhr.sample.repositories;

public class UserNotFoundException extends RuntimeException {
	
	String username;
	
	public UserNotFoundException(String username) {
		super("User not found: " + username);
	}

}
