package com.ranbhr.sample.repositories;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.TypedQuery;

import com.ranbhr.sample.models.Student;

import play.db.jpa.JPAApi;
	
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class JPAStudentRepository implements IStudentRepository{
	
	private final JPAApi jpaApi;
	private final MyExecutionContext context;
	
	@Inject
	public JPAStudentRepository(JPAApi jpaApi, MyExecutionContext context) {
		this.jpaApi = jpaApi;
		this.context = context;
	}

	@Override
	public CompletionStage<Stream<Student>> list() {
		return supplyAsync(() -> jpaApi.withTransaction(em -> {
			TypedQuery<Student> query = em.createNamedQuery("Student.findAll", Student.class);
			return query.getResultList().stream();
		}), context);
	}

	@Override
	public CompletionStage<Student> create(Student postData) {
		return supplyAsync(() -> jpaApi.withTransaction(em -> {
			em.persist(postData);
			return postData;
		}), context);
	}

	@Override
	public CompletionStage<Optional<Student>> get(int id) {
		return supplyAsync(() -> jpaApi.withTransaction(em -> {
			return Optional.ofNullable(em.find(Student.class, id));
		}), context);
	}

	@Override
	public CompletionStage<Optional<Student>> update(int id, Student postData) {
		// TODO Auto-generated method stub
		return null;
	}

}
