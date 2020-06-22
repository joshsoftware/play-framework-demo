package com.ranbhr.sample.services;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ranbhr.sample.controllers.apis.authentication.AuthException;
import com.ranbhr.sample.dtos.LoginDto;
import com.ranbhr.sample.dtos.SystemUserDTO;
import com.ranbhr.sample.repositories.SystemUserRepository;

import play.libs.concurrent.HttpExecutionContext;

@Singleton
public class SystemUserService {
	
	private Logger logger = LoggerFactory.getLogger(SystemUserService.class);
	private SystemUserRepository systemUserRepository;
	private HttpExecutionContext ec;
	
	@Inject
	public SystemUserService(SystemUserRepository systemUserRepository, HttpExecutionContext ec) {
		this.ec = ec;
		this.systemUserRepository = systemUserRepository;
	}
	
	public CompletionStage<SystemUserDTO> findByUsername(String username) {
		return systemUserRepository.findByUsername(username).thenApplyAsync(user -> new SystemUserDTO(user), ec.current());
	}
	
	public CompletionStage<SystemUserDTO> verify(LoginDto loginDto) {
		return systemUserRepository.findByUsername(loginDto.getUsername())
				.thenApplyAsync(user -> { 
					if (user.getPassword().equals(loginDto.getPassword())){
						return new SystemUserDTO(user);
					} else {
						throw new AuthException();
					}
			}, ec.current());
	}

}
