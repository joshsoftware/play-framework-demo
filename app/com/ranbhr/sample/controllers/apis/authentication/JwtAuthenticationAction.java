package com.ranbhr.sample.controllers.apis.authentication;

import static com.ranbhr.sample.utils.JsonResponseGenerator.createResponse;
import static java.util.concurrent.CompletableFuture.completedStage;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import com.ranbhr.sample.controllers.apis.Attrs;
import com.ranbhr.sample.dtos.SystemUserDTO;
import com.ranbhr.sample.repositories.UserNotFoundException;
import com.ranbhr.sample.services.SystemUserService;

import io.jsonwebtoken.ExpiredJwtException;
import play.mvc.Action;
import play.mvc.Http.Request;
import play.mvc.Result;


public class JwtAuthenticationAction extends Action.Simple {

	private SystemUserService userService;
	
	@Inject
	public JwtAuthenticationAction(SystemUserService userService) {
		this.userService = userService;
	}
	
	@Override
	public CompletionStage<Result> call(Request req) {
		Optional<String> authHeader = req.header("Authorization"); 
		if (authHeader.isEmpty() || !authHeader.get().startsWith("Bearer ")) {
			return completedStage(badRequest(createResponse("Invalid authentication header", false)));
		}
		
		String token = authHeader.get().substring("Bearer ".length());
		
		try {
			SystemUserDTO systemUserDTO = JwtClient.getUserFromToken(token);
			SystemUserDTO userDTO = userService.findByUsername(systemUserDTO.getUsername()).toCompletableFuture().get();
			return delegate.call(req.addAttr(Attrs.USER, userDTO));	
		} catch (UserNotFoundException | ExpiredJwtException e) {
			return completedStage(unauthorized(createResponse("Unauthorized",false)));
		} catch ( InterruptedException | ExecutionException  e) {
			return completedStage(internalServerError(createResponse("Something went wrong",false)));
		}
		
	}

}
