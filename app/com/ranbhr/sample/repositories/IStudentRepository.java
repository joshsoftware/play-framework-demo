package com.ranbhr.sample.repositories;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import com.ranbhr.sample.models.Student;

public interface IStudentRepository {
	
	CompletionStage<Stream<Student>> list();

    CompletionStage<Student> create(Student postData);

    CompletionStage<Optional<Student>> get(int id);

    CompletionStage<Optional<Student>> update(int id, Student postData);

}
