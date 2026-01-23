package com.reservas.reservas_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ReservasApiApplication implements CommandLineRunner{

	// Generar un password encriptado con BCrypt

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ReservasApiApplication.class, args);
	}

	public void run(String... args) throws Exception {
		// genere contraseña con Bean PasswordEncoder
		String password = "123456";
		for (int i = 0; i < 3; i++) {
			String passwordEncoded = passwordEncoder.encode(password);
			System.out.println("Password encoded: " + passwordEncoded);

		}
	}
}
