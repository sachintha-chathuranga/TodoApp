package com.sachintha.TodoApp.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachintha.TodoApp.dto.AuthResponse;
import com.sachintha.TodoApp.dto.UserRegistrationDto;
import com.sachintha.TodoApp.service.AuthService;
import com.sachintha.TodoApp.service.JwtService;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthService authService;
	@MockBean
	private JwtService jwtService;

	@Autowired
	private ObjectMapper objectMapper;
	private UserRegistrationDto userRegistrationDto;

	@BeforeEach
	void init() {
		userRegistrationDto = UserRegistrationDto.builder().email("test@gmail.com").password("123abc").build();
	}

	@Test
	public void registerUser_CreateUser_ReturnCreated() throws Exception {

		when(authService.createUser(ArgumentMatchers.any()))
				.thenReturn(ResponseEntity.ok("user registered successfully"));

		ResultActions response = mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRegistrationDto)));
		response.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void loginUser_authUser_ReturnAuthResponse() throws Exception {
		AuthResponse res = AuthResponse.builder().token("ksdfkmkvme451ev15e15ev1e51ve51v5e1ve5").build();

		when(authService.authenticateUser(ArgumentMatchers.any())).thenReturn(res);

		ResultActions response = mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRegistrationDto)));

		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.token", CoreMatchers.is(res.getToken())));
	}
}
