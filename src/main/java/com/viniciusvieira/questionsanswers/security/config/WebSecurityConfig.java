package com.viniciusvieira.questionsanswers.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.viniciusvieira.questionsanswers.security.filter.JWTFilter;
import com.viniciusvieira.questionsanswers.security.filter.LoginFilter;
import com.viniciusvieira.questionsanswers.security.service.UserDetailsServiceImpl;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
	@Autowired
	@Lazy // resolve um proble de dependencia
	private LoginFilter loginFilter;

	@Autowired
	private JWTFilter jwtFilter;

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
		http.cors();
		http.csrf().disable();
		
		http.authorizeRequests()
//		.antMatchers("/login").permitAll()
//		.antMatchers("/swagger-ui.html").permitAll()
//		.antMatchers("/*/professor/**").hasRole("PROFESSOR")
//		.antMatchers("/*/student/**").hasRole("STUDENT")
		.anyRequest()
		.authenticated();
		
		http.addFilterBefore(loginFilter, BasicAuthenticationFilter.class);
		http.addFilterBefore(jwtFilter, BasicAuthenticationFilter.class);
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		return http.build();
	}

}
