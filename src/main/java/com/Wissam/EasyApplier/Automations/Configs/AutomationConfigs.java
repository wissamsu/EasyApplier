package com.Wissam.EasyApplier.Automations.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

@Configuration
public class AutomationConfigs {

  @Bean
  Playwright playwright() {
    return Playwright.create();
  }

  @Bean
  Browser browser(Playwright playwright) {
    return playwright.chromium()
        .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(300 + Math.random() * 1300));
  }

}
