package com.Wissam.EasyApplier.Playwright.Config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    return playwright.chromium()
        .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50).setChannel("chrome"));
  }

  @Bean
  Executor taskExecutor() {
    return Executors.newVirtualThreadPerTaskExecutor();
  }
}
