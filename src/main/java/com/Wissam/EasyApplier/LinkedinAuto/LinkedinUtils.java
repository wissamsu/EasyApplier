package com.Wissam.EasyApplier.LinkedinAuto;

import java.util.List;
import java.util.Optional;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.LiAtCookieInvalidException;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.Cookie;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LinkedinUtils {

  private final UserRepository userRepo;

  public String checkOrgetLiAtCookie(UserDetails userDetails) {
    User user = userRepo.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new UserNotFoundException("User with email " + userDetails.getUsername() + " not found"));
    if (user.getLinkedin() == null) {
      throw new UserNotFoundException("LinkedIn profile not completed for " + userDetails.getUsername());
    }

    if (user.getLinkedin().getEmail().isBlank() || user.getLinkedin().getEmail() == null
        || user.getLinkedin().getPassword().isBlank() || user.getLinkedin().getPassword() == null) {
      throw new UserNotFoundException("Your Linkedin Profile is missing email field");
    }
    if (user.getLinkedin().getLiatCookie().length() < 15) {
      throw new LiAtCookieInvalidException("Your Linkedin Profile's LIAT cookie is invalid");
    }

    if (isLiAtValid(user.getLinkedin().getLiatCookie())) {
      return user.getLinkedin().getLiatCookie();

    }
    try (Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium()
            .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50).setChannel("chrome"));
        BrowserContext ctx = browser.newContext();) {
      Page page = ctx.newPage();
      page.navigate("https://www.linkedin.com/login");
      page.locator("#username").fill(user.getLinkedin().getEmail());
      page.locator("#password").fill(user.getLinkedin().getPassword());
      page.locator("button[type=submit]").click();
      List<Cookie> cookies = ctx.cookies("https://www.linkedin.com/");
      Optional<Cookie> liAtCookie = cookies.stream()
          .filter(c -> c.name.equals("li_at"))
          .findFirst();

      if (liAtCookie.isPresent()) {
        user.getLinkedin().setLiatCookie(liAtCookie.get().value);
        userRepo.save(user);
        return liAtCookie.get().value;
      } else {
        throw new RuntimeException("li_at cookie not found after login");
      }
    } catch (Exception e) {
      throw new RuntimeException("Something went wrong in checkOrGetLiAtCookie error is: ", e);
    }
  }

  private boolean isLiAtValid(String liAt) {
    try {
      Response response = Jsoup.connect("https://www.linkedin.com/voyager/api/me")
          .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
              "(KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36")
          .header("Accept", "application/json, text/plain, */*")
          .header("Referer", "https://www.linkedin.com/")
          .header("Accept-Language", "en-US,en;q=0.9")
          .cookie("li_at", liAt)
          .ignoreContentType(true)
          .method(org.jsoup.Connection.Method.GET)
          .execute();

      int status = response.statusCode();
      return status == 200;
    } catch (Exception e) {
      return false;
    }
  }

}
