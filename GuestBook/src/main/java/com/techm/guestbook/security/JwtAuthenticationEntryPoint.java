package com.techm.guestbook.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		String message;
		log.info("In JwtAuthenticationEntryPoint");

		if(authException.getCause() != null) {
			message = authException.getCause().toString() + " " + authException.getMessage();
		} else {
			message = authException.getMessage();
		}

		byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));

		response.getOutputStream().write(body);
	}

}
