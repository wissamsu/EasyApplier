package com.Wissam.EasyApplier.Sessions;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Playwright;

public record BrowserSession(
    Playwright playwright,
    BrowserContext ctx,
    Browser browser) {
  public void closeAll() {
    ctx.close();
    browser.close();
    playwright.close();
  }
}
