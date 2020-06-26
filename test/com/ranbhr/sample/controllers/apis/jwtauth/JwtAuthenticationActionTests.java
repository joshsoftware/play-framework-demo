package com.ranbhr.sample.controllers.apis.jwtauth;

import org.junit.Before;
import org.junit.Test;
import com.ranbhr.sample.dtos.SystemUserDTO;
import com.ranbhr.sample.repositories.UserNotFoundException;
import com.ranbhr.sample.services.SystemUserService;
import com.ranbhr.sample.utils.Constants;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import play.mvc.Http;
import play.mvc.Result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.*;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class JwtAuthenticationActionTests {
	
	private JwtClient jwtClientMock = mock(JwtClient.class);
	private SystemUserService systemUserServiceMock = mock(SystemUserService.class);
	
	private JwtAuthenticationAction jwtAuthaction = new JwtAuthenticationAction(systemUserServiceMock, jwtClientMock);
	
	@Before
	public void setUp() {
		reset(jwtClientMock, systemUserServiceMock);
	}
	
	@Test
	public void test_withNoAuthHeader() {
 		Http.Request request = mock(Http.Request.class);
		when(request.header("Authorization")).thenReturn(Optional.empty());
		
		CompletionStage<Result> authResult = jwtAuthaction.call(request);
		authResult.whenComplete((result, exception) -> {
			assertNull(exception);
			assertEquals(BAD_REQUEST, result.status());
		});	
	}
	
	@Test
	public void test_withInvalidAuthHeader() {
 		Http.Request request = mock(Http.Request.class);
		when(request.header("Authorization")).thenReturn(Optional.of("RandomValue"));
		
		CompletionStage<Result> authResult = jwtAuthaction.call(request);
		authResult.whenComplete((result, exception) -> {
			assertNull(exception);
			assertEquals(BAD_REQUEST, result.status());
		});	
	}
	
	@Test
	public void test_withInvalidToken() {
		when(jwtClientMock.getUserFromToken(anyString())).thenThrow(JwtException.class);
 		Http.Request request = mock(Http.Request.class);
		when(request.header("Authorization")).thenReturn(Optional.of("Bearer randomToken"));
		assertThrows(AuthException.class, () -> { jwtAuthaction.call(request);});
	}
	
	@Test
	public void test_withExpiredToken() {
		when(jwtClientMock.getUserFromToken(anyString())).thenThrow(ExpiredJwtException.class);
 		Http.Request request = mock(Http.Request.class);
		when(request.header("Authorization")).thenReturn(Optional.of("Bearer randomToken"));
		assertThrows(AuthException.class, () -> { jwtAuthaction.call(request);});
	}
	
	@Test
	public void test_withInvalidUser() {
		SystemUserDTO userDTO = new SystemUserDTO("dummyuser");
		Http.Request request = mock(Http.Request.class);
		when(jwtClientMock.getUserFromToken(anyString())).thenReturn(userDTO);
		when(systemUserServiceMock.findByUsername(anyString())).thenThrow(UserNotFoundException.class);
		
		CompletionStage<Result> authResult = jwtAuthaction.call(request);
		authResult.whenComplete((result, exception) -> {
			assertNotNull(exception);
			assertTrue(exception instanceof UserNotFoundException);
			assertEquals(String.format(Constants.USER_NOT_FOUND, userDTO.getUsername()), exception.getMessage());
		});		
	}

}
