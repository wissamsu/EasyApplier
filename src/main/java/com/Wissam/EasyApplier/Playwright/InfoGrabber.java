package com.Wissam.EasyApplier.Playwright;

import org.springframework.stereotype.Service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InfoGrabber {

  private final Browser browser;

  public boolean tryLinkedinLogin(String email, String password) {
    try (BrowserContext ctx = browser.newContext()) {
      Page page = ctx.newPage();
      page.navigate("https://www.linkedin.com/login");
      page.fill("input[name='session_key']", email);
      page.fill("input[name='session_password']", password);
      page.click("button[type='submit']");
      return true;
    } catch (Exception e) {
      return false;
    }
  }

}
