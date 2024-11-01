package com.sachintha.TodoApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sachintha.TodoApp.security.JwtAuthEntyPoint;
import com.sachintha.TodoApp.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	@Autowired
	private final JwtAuthenticationFilter JwtAuthFilter;
	@Autowired
	private final AuthenticationProvider authenticationProvider;
	@Autowired
	private final JwtAuthEntyPoint authEntyPoint;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/auth/**").permitAll().anyRequest()
						.authenticated())
				.addFilterBefore(JwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.authenticationProvider(authenticationProvider)
				.exceptionHandling(e -> e.authenticationEntryPoint(authEntyPoint));
		return http.build();
	}
}
