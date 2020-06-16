package com.ranbhr.sample.controllers.apis;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import play.mvc.Action;
import play.mvc.Http.Request;
import play.mvc.Result;

import static com.ranbhr.sample.utils.JsonResponseGenerator.*;
import static java.util.concurrent.CompletableFuture.*;

import java.util.Base64;

public class AuthenticationAction extends Action.Simple {

	@Override
	public CompletionStage<Result> call(Request req) {
		Optional<String> basicAuth = req.header("Authorization"); 
		if (basicAuth.isEmpty() || !basicAuth.get().startsWith("Basic ")) {
			return completedStage(badRequest(createResponse("Invalid authentication header", false)));
		}
		
		String authString = new String(Base64.getDecoder().decode(basicAuth.get().substring("Basic ".length())));
		String[] input = authString.split(":");
		if (!"Admin".equals(input[0]) || !"Pwd123".equals(input[1])) {
			return completedStage(unauthorized(createResponse("Unauthenticated", false)));
		}
		
		return delegate.call(req);
	}

}
