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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
		http.cors();
		http.csrf().disable();
		
		jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
		
		http.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/api/login").permitAll()
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
	
	// cors: permite sua aplicaçao receber requisiçao de outros dominios
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
			}
		};
	}
}

/* .allowedOrigins("*") endereço do frontend, nao é uma boa pratica colocar ("*")
 *  o certo seria liberar o acesso 1 por 1 manualmente
 *  
 *  //liberando app cliente 1
    registry.addMapping("/**")
         .allowedOrigins("http://localhost:3000")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");

    //liberando app cliente 2
    registry.addMapping("/topicos/**")
         .allowedOrigins("http://localhost:4000")
        .allowedMethods("GET", "OPTIONS", "HEAD", "TRACE", "CONNECT");
        
        .allowedMethods("*") -> funciona
        
        ## ------------ ## ------------- ##
        
        Outra forma de configurar o AuthenticationManager
        
        @Autowired
    	AuthenticationConfiguration authenticationConfiguration;
    
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(
                authenticationConfiguration.getAuthenticationManager());
                
        // configurando o path para realizar o login
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
 */
