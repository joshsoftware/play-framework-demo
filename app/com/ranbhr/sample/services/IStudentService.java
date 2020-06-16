package com.ranbhr.sample.services;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import com.ranbhr.sample.dtos.StudentDTO;

public interface IStudentService {
	public CompletionStage<Optional<StudentDTO>> findById(int id);
	public CompletionStage<StudentDTO> addStudent(StudentDTO newStudent);
	public CompletionStage<Stream<StudentDTO>> list();
}
