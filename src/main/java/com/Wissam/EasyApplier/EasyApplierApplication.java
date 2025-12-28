package com.Wissam.EasyApplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableJpaAuditing
public class EasyApplierApplication {

  public static void main(String[] args) {
    try {
      Dotenv dotenv = Dotenv.load();
      String jwtSecret = dotenv.get("JWT_SECRET");
      String jwtExpiration = dotenv.get("JWT_EXPIRATION");
      if (jwtSecret != null) {
        System.setProperty("JWT_SECRET", jwtSecret);
      }
      if (jwtExpiration != null) {
        System.setProperty("JWT_EXPIRATION", jwtExpiration);
      }
    } catch (Exception e) {
      System.out.println("Note: .env file not found, using application.properties values");
    }
    SpringApplication.run(EasyApplierApplication.class, args);
  }

}
