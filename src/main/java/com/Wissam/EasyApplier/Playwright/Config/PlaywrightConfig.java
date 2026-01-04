package com.Wissam.EasyApplier.Playwright.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microsoft.playwright.Playwright;

@Configuration
public class PlaywrightConfig {

  @Bean
  Playwright playwright() {
    return Playwright.create();
  }

}
