
package com.solberg.springboot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class ListifyApplication implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(ListifyApplication.class);

  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String googleClientId;

  @Value("${spring.security.oauth2.client.registration.google.client-secret}")
  private String googleClientSecret;

  public static void main(String[] args) {
    SpringApplication.run(ListifyApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    logger.info("Google Client ID: {}", googleClientId);
    logger.info("Google Client Secret: {}", googleClientSecret != null ? "*******" : "Not Set");
  }
}
