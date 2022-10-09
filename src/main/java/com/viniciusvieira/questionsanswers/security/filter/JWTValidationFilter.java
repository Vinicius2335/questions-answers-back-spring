package com.viniciusvieira.questionsanswers.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.viniciusvieira.questionsanswers.security.service.UserDetailsServiceImpl;

@Component
public class JWTValidationFilter extends BasicAuthenticationFilter {
	
	private final UserDetailsServiceImpl userDetailsServiceImpl;
	public static final String HEADER = "Authorization";
	public static final String TOKEN_PREFIX  = "Bearer ";

	public JWTValidationFilter(AuthenticationManager authenticationManager,
			UserDetailsServiceImpl userDetailsServiceImpl) {
		super(authenticationManager);
		this.userDetailsServiceImpl = userDetailsServiceImpl;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String header = request.getHeader(HEADER);
		
		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		String token = header.replace(TOKEN_PREFIX, "");
		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);
		
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(request, response);
		
	}
	
	private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
		String username = JWT.require(Algorithm.HMAC512(JWTAuthenticationFilter.TOKEN_SECRET))
				.build()
				.verify(token)
				.getSubject();
		UserDetails user = userDetailsServiceImpl.loadUserByUsername(username);
		
		return username == null ? null : 
			new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
	}
	
}

