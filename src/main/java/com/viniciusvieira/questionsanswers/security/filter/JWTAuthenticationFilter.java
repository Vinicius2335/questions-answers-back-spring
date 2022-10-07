package com.viniciusvieira.questionsanswers.security.filter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viniciusvieira.questionsanswers.models.ApplicationUserModel;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;
	public static final String TOKEN_SECRET = "${jwt.secret}";
	
	@Override
	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		try {
			ApplicationUserModel user = new ObjectMapper()
					.readValue(request.getInputStream(), ApplicationUserModel.class);
			
			return authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),
							user.getPassword(), user.getAuthorities()));
			
		} catch (IOException e) {
			throw new RuntimeException("Falha ao autenticar usuário", e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, Authentication authResult) throws IOException, ServletException {
		
		ApplicationUserModel user = (ApplicationUserModel)authResult.getPrincipal();
		
		String rolesString = user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		String token = JWT.create()
				.withSubject(user.getUsername())
				.withClaim("roles", rolesString)
				.withExpiresAt(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
				.sign(Algorithm.HMAC512(TOKEN_SECRET));
		
		
		response.getWriter().write(token);
		response.getWriter().flush();
	}

}
