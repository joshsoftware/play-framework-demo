package com.ranbhr.sample.controllers.apis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ranbhr.sample.dtos.StudentDTO;
import com.ranbhr.sample.models.Student;
import com.ranbhr.sample.services.StudentService;

import antlr.collections.List;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.http.client.methods.RequestBuilder;
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
	private String authString = "Basic " + Base64.getEncoder().encodeToString("Admin:Pwd123".getBytes());
	
	@Before
	public void setUp() {
		Mockito.reset(studentServiceMock);
	}
	
	private void withoutAuthentication(String url, String httpMethod) {
		Http.RequestBuilder request = new Http.RequestBuilder()
                .method(httpMethod)
                .uri(url);

        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
	}
	
	private void withInvalidAuthentication(String url, String httpMethod) {
		Http.RequestBuilder request = new Http.RequestBuilder()
                .method(httpMethod)
                .header("Authorization", "notBasicHeader")
                .uri(url);

        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
	}
	
	private void withInvalidCredential(String url, String httpMethod) {
		Http.RequestBuilder request = new Http.RequestBuilder()
                .method(httpMethod)
                .header("Authorization", "Basic "+ Base64.getEncoder().encodeToString("admin:1234".getBytes()))
                .uri(url);

        Result result = route(app, request);
        assertEquals(UNAUTHORIZED, result.status());
	}
	
	@Test 
	public void test_withoutAuthentication() {
		withoutAuthentication("/students", GET);
		withoutAuthentication("/students/1", GET);
		withoutAuthentication("/students", POST);
	}
	
	@Test 
	public void test_withInvalidAuthentication() {
		withInvalidAuthentication("/students", GET);
	}
	
	@Test 
	public void test_withInvalidCredentials() {
		withInvalidCredential("/students", GET);
	}
	
	@Test 
	public void test_retrieve_invalidId() {
		when(studentServiceMock.findById(1)).thenReturn(completedStage(Optional.empty()));
		Http.RequestBuilder request = withAuthentication("/students/1", GET);
		Result result = route(app, request);
		assertEquals(NOT_FOUND, result.status());
	}
	
	@Test 
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
