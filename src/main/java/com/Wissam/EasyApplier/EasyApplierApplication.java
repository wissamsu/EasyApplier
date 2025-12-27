package com.Wissam.EasyApplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableJpaAuditing
public class EasyApplierApplication {

  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.load();
    System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
    System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
    SpringApplication.run(EasyApplierApplication.class, args);
  }

}
