package com.Wissam.EasyApplier.Sessions;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public record LinkedinSession(
    Browser browser,
    BrowserContext context,
    Page page) {
}
