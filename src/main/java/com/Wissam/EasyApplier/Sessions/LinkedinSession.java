package com.Wissam.EasyApplier.Sessions;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public record LinkedinSession(
    Playwright playwright,
    Browser browser,
    BrowserContext context,
    Page page) {
}
