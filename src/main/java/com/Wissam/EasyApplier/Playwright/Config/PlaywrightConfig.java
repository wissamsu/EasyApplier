package com.Wissam.EasyApplier.Playwright.Config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlaywrightConfig {

  @Bean(name = "taskExecutor")
  Executor taskExecutor() {
    return Executors.newVirtualThreadPerTaskExecutor();
  }
}
