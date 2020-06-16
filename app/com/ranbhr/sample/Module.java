package com.ranbhr.sample;

import com.google.inject.AbstractModule;
import com.ranbhr.sample.repositories.IStudentRepository;
import com.ranbhr.sample.repositories.JPAStudentRepository;
import com.ranbhr.sample.repositories.MyExecutionContext;
import com.ranbhr.sample.repositories.StudentRepository;
import com.ranbhr.sample.services.IStudentService;
import com.ranbhr.sample.services.StudentService;

public class Module extends AbstractModule {
	protected void configure() {
		bind(IStudentRepository.class).to(JPAStudentRepository.class);
	    bind(IStudentService.class).to(StudentService.class);
	    //bind(MyExecutionContext.class).to(MyExecutionContext.class);
	}
}
