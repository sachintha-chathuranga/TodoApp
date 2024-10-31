package com.sachintha.TodoApp.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private static final String JWT_SECRET_KEY = "9kGKcmTCOenW0eGCKBszaSp8iiDUHnUB";

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSignInKey()) // <----
				.build().parseSignedClaims(token).getPayload();
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(UserDetails userDetails) {
		Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 15);
		return Jwts.builder().subject(userDetails.getUsername()).signWith(getSignInKey()).expiration(expiration)
				.issuedAt(new Date(System.currentTimeMillis())).compact();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String email = extractEmail(token);
		return (email.equals(userDetails.getUsername()) && !isTokenExpires(token));
	}

	private boolean isTokenExpires(String token) {

		return extractExpiration(token).before(new Date());
	}

	public String extractEmail(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token, Claims::getSubject);
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

}
