package com.viniciusvieira.questionsanswers.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viniciusvieira.questionsanswers.models.ApplicationUserModel;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static com.viniciusvieira.questionsanswers.security.filter.Constants.*;

@AllArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    // responsavel por fazer a autenticaçao do spring security
    private AuthenticationManager authenticationManager;

    // tentando realizar a autenticaçao
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // vai descerializar o json
            ApplicationUserModel user = new ObjectMapper().readValue(request.getInputStream(), ApplicationUserModel.class);
            return authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // caso a autenticaçao seja realizado com sucesso, geramos 1 token.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 1 - definir a validade do token
        ZonedDateTime expTimeUTC = ZonedDateTime.now(ZoneOffset.UTC).plus(EXPIRATION_TIME, ChronoUnit.MILLIS);
        
        String token = Jwts.builder()
                .setSubject(((ApplicationUserModel) authResult.getPrincipal()).getUsername())
                .setExpiration(Date.from(expTimeUTC.toInstant()))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();

        token = TOKEN_PREFIX + token;

        // criando um json {"token": "Bearer token", "exp": "date"}
        String tokenJson = "{\"token\":" + addQuotes(token) + ", \"exp\": "+ addQuotes(expTimeUTC.toString()) +" }";

        response.getWriter().write(tokenJson);
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.addHeader(HEADER_STRING, token);
    }

    // método para adicionar aspas duplas
    private String addQuotes(String value){
        return new StringBuilder(300).append(value).append("\"").toString();
    }
}
