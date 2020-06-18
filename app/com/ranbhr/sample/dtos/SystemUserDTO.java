package com.ranbhr.sample.dtos;

import com.ranbhr.sample.models.RoleEnum;
import com.ranbhr.sample.models.SystemUser;

public class SystemUserDTO {
	
	private String username;
	
	public SystemUserDTO(SystemUser user) {
		this.username = user.getUsername();
	}
	
	public SystemUserDTO (String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	

}
