package com.viniciusvieira.questionsanswers.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEconderUtil {
	public static void main(String[] args) {
		System.out.println("Senha devdojo: " + new BCryptPasswordEncoder().encode("devdojo"));
		System.out.println("Senha senha123: " + new BCryptPasswordEncoder().encode("senha123"));
	}
}
