package com.ranbhr.sample.dtos;

import java.util.List;

public class StudentData {
	private int studentId;
	private List<String> registeredSubjects;
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public List<String> getRegisteredSubjects() {
		return registeredSubjects;
	}
	public void setRegisteredSubjects(List<String> registeredSubjects) {
		this.registeredSubjects = registeredSubjects;
	}
	

}
