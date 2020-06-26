package com.ranbhr.sample.controllers.apis;

import com.fasterxml.jackson.databind.JsonNode;
import com.ranbhr.sample.controllers.apis.jwtauth.AuthException;
import com.ranbhr.sample.controllers.apis.jwtauth.JwtClient;
import com.ranbhr.sample.dtos.StudentDTO;
import com.ranbhr.sample.dtos.SystemUserDTO;
import com.ranbhr.sample.models.Student;
import com.ranbhr.sample.services.StudentService;
import com.ranbhr.sample.services.SystemUserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static java.util.concurrent.CompletableFuture.*;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.*;
import static play.test.Helpers.*;
import static play.test.Helpers.route;

public class StudentControllerTests extends WithApplication{

	private StudentService studentServiceMock = mock(StudentService.class);
	private SystemUserService systemUserService = mock(SystemUserService.class);
	private JwtClient jwtClientMock = mock(JwtClient.class);
	private String authString = "Bearer jwtToken";
	private SystemUserDTO systemUserDto = new SystemUserDTO("username");
	
	@Before
	public void setUp() {
		Mockito.reset(studentServiceMock, jwtClientMock);
		when(jwtClientMock.getUserFromToken(anyString())).thenReturn(systemUserDto);
		when(systemUserService.findByUsername(any())).thenReturn(CompletableFuture.completedStage(systemUserDto));
	}
	
	//@Test 
	public void test_retrieve_invalidId() {	
		when(studentServiceMock.findById(1)).thenReturn(completedStage(Optional.empty()));
		Http.RequestBuilder request = withAuthentication("/students/1", GET);
		Result result = route(app, request);
		assertEquals(NOT_FOUND, result.status());
	}
	
	//@Test 
	public void test_retrieve_validId() {
		when(studentServiceMock.findById(1)).thenReturn(completedStage(Optional.of(new StudentDTO(new Student()))));
		Http.RequestBuilder request = withAuthentication("/students/1", GET);
		Result result = route(app, request);
		assertEquals(OK, result.status());
	}
	
	private Http.RequestBuilder withAuthentication(String url, String method) {
		Http.RequestBuilder request = new Http.RequestBuilder()
                .method(method)
                .header("Authorization", authString)
                .uri(url);
		return request;
	}
	
	//@Test
	public void test_addStudent_valid() {
		StudentDTO studentDTO = new StudentDTO("fname", "lname");
		when(studentServiceMock.addStudent(studentDTO)).thenReturn(completedStage(studentDTO));
		Http.RequestBuilder request = withAuthentication("/students",POST);
		request.bodyJson(Json.toJson(studentDTO));
		Result result = route(app, request);
		assertEquals(CREATED, result.status());
		
	}
	
	//@Test
	public void test_findAll_valid() {
		StudentDTO studentDTO = new StudentDTO("fname", "lname");
		studentDTO.setId(1);
		when(studentServiceMock.list()).thenReturn(completedStage(Stream.of(studentDTO)));
		Http.RequestBuilder request = withAuthentication("/students", GET);
		Result result = route(app, request);
		assertEquals(OK, result.status());
		JsonNode data = Json.toJson(result.body());
		assertNotNull(data);
		assertNotNull(data.findPath("data"));
		assertNotNull(Json.fromJson(data.findPath("data").get(1),StudentDTO.class));
	}
	
}
