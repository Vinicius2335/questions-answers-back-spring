package com.viniciusvieira.questionsanswers.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.viniciusvieira.questionsanswers.security.filter.JWTAuthenticationFilter;
import com.viniciusvieira.questionsanswers.security.filter.JWTValidationFilter;
import com.viniciusvieira.questionsanswers.security.service.UserDetailsServiceImpl;


@EnableWebSecurity
public class WebSecurityConfig {
	@Autowired
	@Lazy
	private JWTAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	@Lazy
	private JWTValidationFilter jwtValidationFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder,
			UserDetailsServiceImpl userDetailService) throws Exception {

		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailService)
				.passwordEncoder(passwordEncoder).and().build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.cors(); como personalizamos o cors abaixo, nao precisa mais disso
		http.csrf().disable();
		
		http.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/login").permitAll()
		.antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui/index.html")
		.permitAll()
		.antMatchers("/*/professor/**").hasRole("PROFESSOR")
		.antMatchers("/*/student/**").hasRole("STUDENT")
		.anyRequest()
		.authenticated();
		
		http.addFilter(jwtAuthenticationFilter);
		http.addFilter(jwtValidationFilter);
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		return http.build();
	}
	
	// TEST: não sei se funciona
	// cors: permite sua aplicaçao receber requisiçao de outros dominios
	@Bean
	UrlBasedCorsConfigurationSource corsConfigurationSource() {
		final  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		source.registerCorsConfiguration("/**", corsConfiguration);
		
		return source;
	}

}
