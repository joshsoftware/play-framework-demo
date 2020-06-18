package com.ranbhr.sample.services;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.ranbhr.sample.dtos.SystemUserDTO;
import com.ranbhr.sample.repositories.SystemUserRepository;

import play.libs.concurrent.HttpExecutionContext;

@Singleton
public class SystemUserService {
	
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

}
