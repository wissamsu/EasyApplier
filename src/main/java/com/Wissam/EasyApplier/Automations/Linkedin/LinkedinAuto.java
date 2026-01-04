package com.Wissam.EasyApplier.Automations.Linkedin;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.Wissam.EasyApplier.Sessions.LinkedinSession;
import com.Wissam.EasyApplier.Utils.LinkedinUtils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.Proxy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class LinkedinAuto {

  private final LinkedinUtils linkedinUtils;
  private final UserRepository userRepo;
  private final Playwright playwright;
  private final LinkedinScripts linkedinScripts;

  Proxy proxy = new Proxy("http://142.111.48.253:7030")
      .setUsername("jztdgogd")
      .setPassword("94vn6lv3dieu");

  public LinkedinSession login(UserDetails userDetails) {
    try {
      User user = userRepo.findByEmail(userDetails.getUsername())
          .orElseThrow(() -> new UserNotFoundException("User with email " + userDetails.getUsername() + " not found"));
      linkedinUtils.checkOrgetLiAtCookie(userDetails);

      Browser browser = playwright.chromium()
          .launch(
              new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50).setProxy(proxy));
      BrowserContext ctx = browser.newContext();
      Cookie liAt = new Cookie("li_at", user.getLinkedin().getLiatCookie());
      liAt.setDomain(".linkedin.com");
      liAt.setPath("/");
      liAt.setSecure(true);
      liAt.setHttpOnly(true);
      ctx.addCookies(List.of(liAt));

      Page page = ctx.newPage();
      page.navigate("https://www.linkedin.com/feed");
      page.waitForTimeout(5000);
      return new LinkedinSession(playwright, browser, ctx, page);
    } catch (Exception e) {
      log.error("Error while logging in method login in class LinkedinAuto: " + e.getMessage());
      return null;
    }
  }

  @Async("taskExecutor")
  public void autoConnect(User user) {
    LinkedinSession session = login(user);
    Page page = session.page();
    page.navigate("https://www.linkedin.com/mynetwork/grow/");

    int invited = 0;
    while (invited < 100) {
      Locator buttons = page.locator("button:has(svg#connect-small)");
      int count = buttons.count();

      if (count == 0) {
        System.out.println("No more buttons found, scrolling...");
        page.waitForTimeout(1000);
        continue;
      }

      for (int i = 2; i < count && invited < 100; i++) {
        try {
          buttons.nth(i).click();
          page.mouse().wheel(0, 100);

          invited++;
          System.out.println("Invited " + invited);
          page.waitForTimeout(500);
        } catch (PlaywrightException e) {
          System.out.println("Failed to click button: " + e.getMessage());
        }
      }
    }
    linkedinUtils.sessionCloser(session);
  }

  public void getJobsList(String jobTitle, User user) throws IOException {
    Browser browser = playwright.chromium()
        .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50).setProxy(proxy));
    BrowserContext context = browser.newContext();
    Cookie cookie = new Cookie("li_at", user.getLinkedin().getLiatCookie());
    cookie.setUrl("https://www.linkedin.com");
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    context.addCookies(List.of(cookie));
    Page page = context.newPage();
    page.navigate("https://www.linkedin.com/jobs/search/?keywords=" + URLEncoder.encode(jobTitle, "UTF-8") + "&f_EA=1");

    Locator jobCards = page.locator("li[data-occludable-job-id]");
    log.info("Found " + jobCards.count() + " jobs");

    for (int i = 0; i < jobCards.count(); i++) {
      Locator jobCard = jobCards.nth(i);
      jobCard.click();
      linkedinScripts.EasyApplyScript(page);
      page.waitForTimeout(100);
    }

    page.waitForTimeout(5000);
    page.close();
    browser.close();
  }

}
