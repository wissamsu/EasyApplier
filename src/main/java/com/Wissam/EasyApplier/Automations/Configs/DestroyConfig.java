package com.Wissam.EasyApplier.Automations.Configs;

import org.springframework.stereotype.Component;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DestroyConfig {

  private final Playwright playwright;
  private final Browser browser;

  @PreDestroy
  public void destroy() {
    browser.close();
    playwright.close();
  }

}
