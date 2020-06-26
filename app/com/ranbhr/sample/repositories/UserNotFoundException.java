package com.ranbhr.sample.repositories;

import com.ranbhr.sample.utils.Constants;

public class UserNotFoundException extends RuntimeException {
	
	String username;
	
	public UserNotFoundException(String username) {
		super(String.format(Constants.USER_NOT_FOUND, username));
	}

}
