package com.ranbhr.sample.controllers.apis;

import static org.mockito.Mockito.mock;
import static play.test.Helpers.route;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.ranbhr.sample.controllers.apis.jwtauth.JwtClient;
import com.ranbhr.sample.dtos.SystemUserDTO;
import com.ranbhr.sample.services.StudentService;
import com.ranbhr.sample.services.SystemUserService;

import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static play.mvc.Http.Status.*;
import static play.test.Helpers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class SessionControllerTests extends WithApplication{
	private SystemUserService userServiceMock = mock(SystemUserService.class);
	private JwtClient jwtClientMock = mock(JwtClient.class);
	private static String loginData = "{"
			+ "\"username\" : \"%s\","
			+ "\"password\" : \"%s\""
			+ "}";
	
	@Test
	public void test_generateToken() {
		SystemUserDTO userDTO = new SystemUserDTO("testuser");
		when(userServiceMock.verify(any())).thenReturn(CompletableFuture.completedStage(userDTO));
		Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/token/generate")
                .bodyJson(Json.parse(String.format(loginData, "testuser", "pwd123")));

        Result result = route(app, request);
        assertEquals(OK, result.status());
        
        JsonNode data = Json.toJson(result.body());
        assertNotNull(data.path("data").asText());
	}
}
