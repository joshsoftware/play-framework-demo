package com.ranbhr.sample.controllers.apis;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

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
	
	@Inject
	public SessionController(HttpExecutionContext context, SystemUserService userService) {
		this.ec = context;
		this.userService = userService;
	}

	public CompletionStage<Result> generateToken(Http.Request request) {
		LoginDto loginDto = Json.fromJson(request.body().asJson(), LoginDto.class);
		try {
			return userService.findByUsername(loginDto.getUsername()).thenApplyAsync(user -> {
				return ok(createResponse(JwtClient.generateToken(user), true));	
			});
		} catch (UserNotFoundException e) {
			return completedStage(unauthorized(createResponse("Unauthorized",false)));
		}
	}
}
