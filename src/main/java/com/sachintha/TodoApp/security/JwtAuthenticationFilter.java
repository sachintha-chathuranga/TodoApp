package com.sachintha.TodoApp.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sachintha.TodoApp.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private final JwtService jwtService;
	@Autowired
	private final UserDetailsService userDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String token;
		final String email;
		logger.info("Request Start to Authorize");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			logger.info("Token Not found in Header");
			filterChain.doFilter(request, response);
			return;
		}
		logger.info("Start Token validation");
		token = authHeader.substring(7);
		email = jwtService.extractEmail(token);
		logger.trace("Email Found in token: {}", email);
		// if token have an email but not authenticated for current ongoing request,
		// this will authenticate token
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// check user actual register user or not
			UserDetails userDetails = this.userDetailService.loadUserByUsername(email);
			// check token expired or has it valid secret key
			if (jwtService.validateToken(token, userDetails)) {
				logger.info("Token validate successfuly");
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}

		}
		logger.info("End of authentication");
		filterChain.doFilter(request, response);
	}

}
