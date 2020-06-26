package com.ranbhr.sample.controllers.apis.jwtauth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import com.ranbhr.sample.dtos.SystemUserDTO;
import com.ranbhr.sample.models.RoleEnum;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Singleton
public class JwtClient {
	private static final String JWT_SECRET = "jwtsecret";
	private static final long TOKEN_EXPIRY = 2*60*1000;
	
	public String generateToken(SystemUserDTO user) {
		long currentTime = System.currentTimeMillis();
	    return Jwts.builder()
		.setSubject(user.getUsername())
		.setIssuedAt(new Date(currentTime))
		.setExpiration(new Date(currentTime + TOKEN_EXPIRY))
		.signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
	}
	
	public SystemUserDTO getUserFromToken(String jwtToken) {
		Claims claims = getAllClaims(jwtToken);
		return new SystemUserDTO(claims.getSubject()); 
	}
	
	private Claims getAllClaims(String jwtToken) {
		return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwtToken).getBody();
	}
}
