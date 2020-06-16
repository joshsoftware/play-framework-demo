package com.ranbhr.sample.repositories;

import java.util.HashMap;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.ranbhr.sample.models.Student;

import play.libs.concurrent.HttpExecutionContext;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class StudentRepository implements IStudentRepository {
	private Map<Integer, Student> students = new HashMap<>();
	private MyExecutionContext ec;
	
	@Inject
	public StudentRepository(MyExecutionContext context) {
		this.ec = context;
	}
	public Optional<Student> findById(int id) {
		return Optional.ofNullable(students.get(id));
	}
	
	public Student save(Student student) {
		int id = students.size()+1;
		student.setId(id);
		students.put(id, student);
		return student;
	}

	@Override
	public CompletionStage<Stream<Student>> list() {
		return supplyAsync(() -> students.values().stream(), ec);
	}

	
	@Override
	public CompletionStage<Student> create(Student student) {
		return supplyAsync(() -> save(student), ec);
	}

	@Override
	public CompletionStage<Optional<Student>> get(int id) {
		return supplyAsync(() -> findById(id), ec);
	}

	@Override
	public CompletionStage<Optional<Student>> update(int id, Student postData) {
		// TODO Auto-generated method stub
		return null;
	}
}
