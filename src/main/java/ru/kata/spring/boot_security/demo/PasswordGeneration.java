package ru.kata.spring.boot_security.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGeneration {
        public static void main(String[] args) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String rawPassword = "admin";
            String hashedPassword = passwordEncoder.encode(rawPassword);
            System.out.println(hashedPassword);
        }
    }

