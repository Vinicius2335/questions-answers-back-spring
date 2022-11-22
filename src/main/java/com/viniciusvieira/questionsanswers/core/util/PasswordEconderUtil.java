package com.viniciusvieira.questionsanswers.core.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEconderUtil {
	public static void main(String[] args) {
		System.out.println("Senha devdojo: " + new BCryptPasswordEncoder().encode("devdojo"));
		System.out.println("Senha senha123: " + new BCryptPasswordEncoder().encode("senha123"));
		System.out.println("Senha estudante: " + new BCryptPasswordEncoder().encode("estudante"));
		
		//String body = "{\"username\":\"vinicius\",\"password\":\"devdojo\"}";
		//System.out.println(body);
		
		System.out.println(RandomStringUtils.randomAlphanumeric(6));
	}
}
