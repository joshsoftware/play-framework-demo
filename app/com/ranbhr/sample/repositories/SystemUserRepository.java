package com.ranbhr.sample.repositories;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.ranbhr.sample.dtos.SystemUserDTO;
import com.ranbhr.sample.models.Student;
import com.ranbhr.sample.models.SystemUser;

import play.db.jpa.JPAApi;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.concurrent.CompletableFuture;

@Singleton
public class SystemUserRepository {

	private final JPAApi jpaApi;
	private final MyExecutionContext ec;
	
	@Inject
	public SystemUserRepository(JPAApi jpaApi, MyExecutionContext ec) {
		this.jpaApi = jpaApi;
		this.ec = ec;
	}
	
	public CompletionStage<SystemUser> findByUsername(String username) {
		return supplyAsync(() -> jpaApi.withTransaction(em -> {
			try {
				TypedQuery<SystemUser> query = em.createNamedQuery("SystemUser.findByUsername", SystemUser.class);
				query.setParameter("username", username);
				return query.getSingleResult();
			}
			catch (NoResultException e) {
				throw new UserNotFoundException(username);
			}
		}));
	}
	
}
