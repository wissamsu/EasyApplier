package com.Wissam.EasyApplier.Playwright.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

@Configuration
public class PlaywrightConfig {

  @Bean(destroyMethod = "close")
  Playwright playwright() {
    return Playwright.create();
  }

  @Bean(destroyMethod = "close")
  Browser browser(Playwright playwright) {
    return playwright().chromium()
        .launch(
            new BrowserType.LaunchOptions().setHeadless(true).setSlowMo(300 + Math.random() * 1300));
  }

}
