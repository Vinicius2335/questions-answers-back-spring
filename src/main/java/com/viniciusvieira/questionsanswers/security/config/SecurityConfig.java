package com.viniciusvieira.questionsanswers.security.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import com.viniciusvieira.questionsanswers.security.filter.JWTAuthenticationFilter;
import com.viniciusvieira.questionsanswers.security.filter.JWTAuthorizationFilter;
import com.viniciusvieira.questionsanswers.security.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

// TODO: Classe depreciada
@EnableWebSecurity
@RequiredArgsConstructor
@SuppressWarnings("deprecation")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService customUserDetailsService;

    // CORS -> quando algum dia utilizarmos uma aplicaçao javaSript
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> new CorsConfiguration()
                .applyPermitDefaultValues())
                .and().csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/*/professor/**").hasRole("PROFESSOR") // TODO: SE DER ERRO É ROLE_PROFESSOR
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), customUserDetailsService));
    }

    // SERVE PARA VALIDAR OS PASSWORDS CRIPTOGRAFADOS
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
