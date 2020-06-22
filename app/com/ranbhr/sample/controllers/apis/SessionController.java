package com.ranbhr.sample.controllers.apis;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.ranbhr.sample.controllers.apis.authentication.AuthException;
import com.ranbhr.sample.controllers.apis.authentication.JwtClient;
import com.ranbhr.sample.dtos.LoginDto;
import com.ranbhr.sample.repositories.UserNotFoundException;
import com.ranbhr.sample.services.SystemUserService;

import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import static java.util.concurrent.CompletableFuture.*;
import static com.ranbhr.sample.utils.JsonResponseGenerator.*;

public class SessionController extends Controller {
	

	private HttpExecutionContext ec;
	private SystemUserService userService;
	private JwtClient jwtClient;

	@Inject
	public SessionController(HttpExecutionContext context, SystemUserService userService, JwtClient jwtClient) {
		this.ec = context;
		this.userService = userService;
		this.jwtClient = jwtClient;
	}

	public CompletionStage<Result> generateToken(Http.Request request) {
		LoginDto loginDto = Json.fromJson(request.body().asJson(), LoginDto.class);
		return userService.findByUsername(loginDto.getUsername())
		.exceptionally(exception -> {
			throw new AuthException();
		}).thenApplyAsync(user -> {
			return ok(createResponse(jwtClient.generateToken(user), true));
		});
		/*
		 * try { return
		 * userService.findByUsername(loginDto.getUsername()).thenApplyAsync(user -> {
		 * return ok(createResponse(jwtClient.generateToken(user), true)); }); } catch
		 * (Exception e) { return
		 * completedStage(unauthorized(createResponse("Invalid credentials",false))); }
		 * 
		 */ 
	}
}
