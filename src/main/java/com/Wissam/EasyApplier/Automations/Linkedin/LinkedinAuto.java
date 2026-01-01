package com.Wissam.EasyApplier.Automations.Linkedin;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.Wissam.EasyApplier.Utils.LinkedinUtils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.Proxy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LinkedinAuto {

  private final LinkedinUtils linkedinUtils;
  private final UserRepository userRepo;

  public void login(UserDetails userDetails) {
    User user = userRepo.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new UserNotFoundException("User with email " + userDetails.getUsername() + " not found"));
    linkedinUtils.checkOrgetLiAtCookie(userDetails);
    Proxy proxy = new Proxy("http://142.111.48.253:7030")
        .setUsername("jztdgogd")
        .setPassword("94vn6lv3dieu");
    try (Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium()
            .launch(
                new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50).setChannel("chrome").setProxy(proxy));
        BrowserContext ctx = browser.newContext();) {
      Cookie liAt = new Cookie("li_at", user.getLinkedin().getLiatCookie());
      liAt.setDomain(".linkedin.com");
      liAt.setPath("/");
      liAt.setSecure(true);
      liAt.setHttpOnly(true);
      ctx.addCookies(List.of(liAt));

      Page page = ctx.newPage();
      page.navigate("https://www.linkedin.com/feed");
      page.waitForTimeout(5000);
    }
  }

}
