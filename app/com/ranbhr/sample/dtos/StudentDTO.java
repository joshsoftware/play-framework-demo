package com.ranbhr.sample.dtos;

import java.util.Date;

import com.ranbhr.sample.models.Student;

public class StudentDTO {
	private Integer id;
	private String firstName;
	private String lastName;
	private Date createdAt;

	public StudentDTO() {}

	public StudentDTO(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public StudentDTO(Student student) {
		this.id = student.getId();
		this.firstName = student.getFirstName();
		this.lastName = student.getLastName();
		this.createdAt = student.getCreateAt();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdBy) {
		this.createdAt = createdBy;
	}
}
