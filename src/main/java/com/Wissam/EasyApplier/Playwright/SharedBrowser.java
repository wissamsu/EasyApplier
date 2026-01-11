// package com.Wissam.EasyApplier.Playwright;
//
// import org.springframework.stereotype.Component;
//
// import com.microsoft.playwright.Browser;
// import com.microsoft.playwright.BrowserType;
// import com.microsoft.playwright.Playwright;
//
// @Component
// public class SharedBrowser {
// private final Playwright playwright;
// private final Browser browser;
//
// public SharedBrowser() {
// this.playwright = Playwright.create();
// this.browser = playwright.chromium()
// .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(300));
// }
//
// public Browser getBrowser() {
// return browser;
// }
// }
