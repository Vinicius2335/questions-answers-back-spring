package com.viniciusvieira.questionsanswers.security.filter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.TimeUnit;

public class Constants {
    public static final String SECRET = "secre";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 86400000L; // 1 dia

    public static void main(String[] args) {
        // Convertendo 1 dia para milisegundos
        System.out.println(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        System.out.println(new BCryptPasswordEncoder().encode("devdojo"));
    }
}
