package com.ranbhr.sample.controllers.apis.jwtauth;

import static com.ranbhr.sample.utils.JsonResponseGenerator.createResponse;
import static java.util.concurrent.CompletableFuture.*;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;

import com.ranbhr.sample.controllers.apis.Attrs;
import com.ranbhr.sample.dtos.SystemUserDTO;
import com.ranbhr.sample.services.SystemUserService;

import io.jsonwebtoken.JwtException;
import play.mvc.Action;
import play.mvc.Http.Request;
import play.mvc.Result;


public class JwtAuthenticationAction extends Action.Simple {

	private SystemUserService userService;
	private JwtClient jwtClient; 
	
	@Inject
	public JwtAuthenticationAction(SystemUserService userService, JwtClient jwtClient) {
		this.userService = userService;
		this.jwtClient = jwtClient;
	}
	
	@Override
	public CompletionStage<Result> call(Request req) {
		Optional<String> authHeader = req.header("Authorization"); 
		if (authHeader.isEmpty() || !authHeader.get().startsWith("Bearer ")) {
			return completedStage(badRequest(createResponse("Invalid authentication header", false)));
		}
		SystemUserDTO token = null;
		
		try {
			token = jwtClient.getUserFromToken(authHeader.get().substring("Bearer ".length()));
		} catch (JwtException e) {
			throw new AuthException();
		}
		
		return userService.findByUsername(token.getUsername())
				.thenCompose(user -> { return delegate.call(req.addAttr(Attrs.USER, user));});	
		
	}

}
