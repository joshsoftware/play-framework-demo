package com.ranbhr.sample;

import static com.ranbhr.sample.utils.JsonResponseGenerator.createResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.ranbhr.sample.controllers.apis.authentication.AuthException;
import com.ranbhr.sample.repositories.UserNotFoundException;
import com.typesafe.config.Config;

import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.http.HttpErrorHandler;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler {

	@Inject
	public ErrorHandler(Config config, Environment environment, OptionalSourceMapper sourceMapper,
			Provider<Router> routes) {
		super(config, environment, sourceMapper, routes);
		// TODO Auto-generated constructor stub
	}

	public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
//        Logger.error(AppUtils.concatStrings("Exception: method: ", request.path(), " time:", DateTime.now().toString(),
  //              " uri=", request.uri(), " remote-address=", request.remoteAddress(), "cause: ", exception.getMessage()));
		Throwable cause = exception.getCause();
		
        if (cause instanceof AuthException) {
            return CompletableFuture.completedFuture(
                    Results.unauthorized(createResponse(cause.getMessage(), false)));
        } 
        
        if (cause instanceof UserNotFoundException) {
        	return CompletableFuture.completedFuture(
                    Results.unauthorized(createResponse(cause.getMessage(), false)));
        }
        
        return CompletableFuture.completedFuture(
                Results.internalServerError(createResponse("Something went wrong", false)));
    }

	@Override
	public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
		return CompletableFuture.completedFuture(
		        Results.status(statusCode, createResponse(message, false)));
	}
}
