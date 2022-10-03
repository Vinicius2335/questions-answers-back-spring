package com.viniciusvieira.questionsanswers.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEconderUtil {
	public static void main(String[] args) {
		System.out.println("Senha: " + new BCryptPasswordEncoder().encode("devdojo"));
	}
}
