package com.Wissam.EasyApplier.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.Cookie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class LinkedinUtils {

  private final UserRepository userRepo;
  private final Browser browser;

  public String checkOrgetLiAtCookie(UserDetails userDetails) {
    User user = userRepo.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new UserNotFoundException("User with email " + userDetails.getUsername() + " not found"));
    System.out.println("LiAt cookie is: " + user.getLinkedin().getLiatCookie());
    if (user.getLinkedin() == null) {
      throw new UserNotFoundException("LinkedIn profile not completed for " + userDetails.getUsername());
    }

    if (user.getLinkedin().getEmail() == null || user.getLinkedin().getEmail().isBlank()
        || user.getLinkedin().getPassword() == null || user.getLinkedin().getPassword().isBlank()) {
      throw new UserNotFoundException("Your Linkedin Profile is missing email field");
    }

    if (isLiAtValid(user.getLinkedin().getLiatCookie())) {
      System.out.println("LIAT cookie is valid");
      return user.getLinkedin().getLiatCookie();
    }
    // Proxy proxy = new Proxy("23.95.150.145:6114")
    // .setUsername("jztdgogd")
    // .setPassword("94vn6lv3dieu");
    try (
        BrowserContext ctx = browser.newContext();) {
      Page page = ctx.newPage();
      page.navigate("https://www.linkedin.com/login");
      page.locator("#username").fill(user.getLinkedin().getEmail());
      page.locator("#password").fill(user.getLinkedin().getPassword());
      page.locator("button[type=submit]").click();
      page.waitForTimeout(5000);
      List<Cookie> cookies = ctx.cookies("https://www.linkedin.com/");
      Optional<Cookie> liAtCookie = cookies.stream()
          .filter(c -> c.name.equals("li_at"))
          .findFirst();

      if (liAtCookie.isPresent()) {
        user.getLinkedin().setLiatCookie(liAtCookie.get().value);
        userRepo.save(user);
      } else {
        throw new RuntimeException("li_at cookie not found after login");
      }

      return liAtCookie.get().value;
    } catch (Exception e) {
      throw new RuntimeException("Something went wrong in checkOrGetLiAtCookie error is: ", e);
    }
  }

  private boolean isLiAtValid(String liAt) {
    if (liAt == null || liAt.isBlank()) {
      System.out.println("li_at cookie is missing");
      return false;
    }
    try {
      Response response = Jsoup.connect("https://www.linkedin.com/feed/")
          .userAgent("Mozilla/5.0")
          .cookie("li_at", liAt)
          .followRedirects(true)
          .method(org.jsoup.Connection.Method.GET)
          .execute();

      String finalUrl = response.url().toString();
      if (finalUrl.contains("/login")) {
        System.out.println("Redirected to login");
        return false;
      }

      String body = response.body();
      if (body.contains("Sign in") || body.contains("session_redirect")) {
        System.out.println("Found sign in or session redirect");
        return false;
      }

      System.out.println("LIAT cookie is valid");
      return response.statusCode() == 200;

    } catch (Exception e) {
      log.error("Error while checking LIAT cookie: ", e.getMessage());
      return false;
    }
  }

  public Path getContextPath(UUID userId) {
    Path dir = Paths.get("contexts");
    try {
      Files.createDirectories(dir);
    } catch (IOException e) {
      throw new RuntimeException("Cannot create contexts folder", e);
    }
    return dir.resolve("user-" + userId + "-linkedin.json");
  }

  public BrowserContext createOrLoadContext(Path statePath, Browser browser) {
    NewContextOptions options = new NewContextOptions();
    if (Files.exists(statePath)) {
      options.setStorageStatePath(statePath);
    }
    return browser.newContext(options);
  }

  public void saveContext(BrowserContext ctx, Path statePath) {
    ctx.storageState(new BrowserContext.StorageStateOptions().setPath(statePath));
  }

}
