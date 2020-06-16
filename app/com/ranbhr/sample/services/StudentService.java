package com.ranbhr.sample.services;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.ranbhr.sample.dtos.StudentDTO;
import com.ranbhr.sample.models.Student;
import com.ranbhr.sample.repositories.IStudentRepository;

import play.libs.concurrent.HttpExecutionContext;

@Singleton
public class StudentService implements IStudentService{
	
	private IStudentRepository studentRepository;
	private HttpExecutionContext ec;

	@Inject
	public StudentService(IStudentRepository studentRepository, HttpExecutionContext context ) {
		this.studentRepository = studentRepository;
		this.ec = context;
	}
	@Override
	public CompletionStage<Optional<StudentDTO>> findById(int id) {
		return studentRepository.get(id).thenApplyAsync(student -> { 
			return student.map(student1 -> new StudentDTO(student1));
		}, ec.current());
	}

	@Override
	public CompletionStage<StudentDTO> addStudent(StudentDTO studentDTO) {
		final Student newStudent = new Student(studentDTO.getFirstName(), studentDTO.getLastName());
		newStudent.setCreateAt(new Date());
		return studentRepository.create(newStudent).thenApplyAsync(student ->  
			new StudentDTO(student), ec.current());
	}

	@Override
	public CompletionStage<Stream<StudentDTO>> list() {
		return studentRepository.list().thenApplyAsync(studentStream -> {
			return studentStream.map(student -> new StudentDTO(student));
		}, ec.current());
	}

}
