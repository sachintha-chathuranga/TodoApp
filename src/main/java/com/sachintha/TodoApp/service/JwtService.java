package com.sachintha.TodoApp.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sachintha.TodoApp.dto.UserDto;
import com.sachintha.TodoApp.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	@Value("${security.jwt.secret-key}")
	private String JWT_SECRET_KEY;
	private long tokenLifeTime = 1000 * 60 * 60; // 1000=1sec, 1000*60=1min, 1000*60*60=1hour

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(UserDto userDetails) {
		Date expiration = new Date(System.currentTimeMillis() + tokenLifeTime);
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", userDetails.getId());
		claims.put("role", userDetails.getRole());
		return Jwts.builder().subject(userDetails.getEmail()).signWith(getSignInKey()).expiration(expiration)
				.issuedAt(new Date(System.currentTimeMillis())).claims(claims).compact();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String email = extractEmail(token);
		return (email.equals(userDetails.getUsername()) && !isTokenExpires(token));
	}

	private boolean isTokenExpires(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public User getUserFromJwt() {
		UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder
				.getContext().getAuthentication();
		User user = (User) authToken.getPrincipal();
		return user;
	}
}
