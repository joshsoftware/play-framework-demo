package com.ranbhr.sample.controllers.apis;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static com.ranbhr.sample.utils.JsonResponseGenerator.*;

import java.util.ArrayList;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.ranbhr.sample.controllers.apis.authentication.JwtAuthenticationAction;
import com.ranbhr.sample.dtos.StudentDTO;
import com.ranbhr.sample.services.IStudentService;
import com.ranbhr.sample.utils.Constants;
import com.ranbhr.sample.utils.JsonResponseGenerator;
import com.typesafe.config.Config;

import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;

@Singleton
public class StudentController extends Controller{
	private WSClient wsClient;
	private HttpExecutionContext ec;
	private IStudentService studentService;
	private Config config;
	
	@Inject
	public StudentController(HttpExecutionContext httpExecutionContext,
			IStudentService studentService
			,WSClient wsClient
			, Config config
			){
		this.ec = httpExecutionContext;
		this.studentService = studentService;
		this.wsClient = wsClient;
		this.config = config;
	}
	
	@With(JwtAuthenticationAction.class)
	public CompletionStage<Result> retrieve(int id) {
		return studentService.findById(id).
				thenApplyAsync(studentOptional -> {
			return studentOptional.map(student -> ok(JsonResponseGenerator.createResponse(student,true)))
			.orElseGet(() -> notFound(JsonResponseGenerator.createResponse("Entity does not exists", false)));
			
		}, ec.current());
	    
	}
	
	@With(JwtAuthenticationAction.class)
	public CompletionStage<Result> addStudent(Http.Request request) {
		StudentDTO student = Json.fromJson(request.body().asJson(), StudentDTO.class);
		return studentService.addStudent(student).thenApplyAsync(newStudent -> {
			return created(JsonResponseGenerator.createResponse(newStudent,true));
		}, ec.current());
	}
	
	@With(JwtAuthenticationAction.class)
	public CompletionStage<Result> getStudentData(int id) {
		return wsClient.url(config.getString("ws.students.data") 
				+ String.format(Constants.studentDataUrl,id))
				.get()
				.thenApplyAsync((WSResponse r) -> {
		              return ok(r.asJson());
		            }, ec.current());
	}
	
	@With(JwtAuthenticationAction.class)
	public CompletionStage<Result> findAll() {
		return studentService.list().thenApplyAsync(students -> {
			return ok(createResponse(students.collect(Collectors.toList()), true));
		}, ec.current());
	}
	
}
