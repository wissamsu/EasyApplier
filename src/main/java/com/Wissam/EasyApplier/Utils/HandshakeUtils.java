package com.Wissam.EasyApplier.Utils;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Model.User;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class HandshakeUtils {

  private final Browser browser;

  public void oktaLogin(User user) {
    Path path = getContextPath(UUID.fromString(user.getUuid()));
    try (BrowserContext context = createOrLoadContext(path, browser); Page page = context.newPage();) {
      page.navigate("https://app.joinhandshake.com/login?requested_authentication_method=standard");
      page.waitForLoadState(LoadState.DOMCONTENTLOADED);
      page.getByPlaceholder("Enter email").fill(user.getHandshake().getEmail());
      page.mouse().click(300, 400);
      page.locator(".primary").click();
      page.waitForSelector("a[data-track-click='login-v2-clicked-sso-recommended']");
      page.locator("a[data-track-click='login-v2-clicked-sso-recommended']").click();
      page.waitForURL(url -> url.contains(".edu"));
      page.locator("//input").first().fill(user.getHandshake().getEmail());
      page.locator("input[data-type='save']").click();
      page.waitForLoadState(LoadState.DOMCONTENTLOADED);
      page.locator("input[type='password']").fill(user.getHandshake().getPassword());
      page.locator("input[data-type='save']").click();
      page.locator("a[data-se='button']").nth(1).click();
      page.waitForTimeout(20000);
      saveContext(context, path);
    } catch (Exception e) {
      log.error("Error logging in: " + e.getMessage());
    }
  }

  public Path getContextPath(UUID userId) {
    Path dir = Paths.get("contexts");
    try {
      Files.createDirectories(dir);
    } catch (IOException e) {
      throw new RuntimeException("Cannot create contexts folder", e);
    }
    return dir.resolve("user-" + userId + "-handshake.json");
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

  public String extractJobId(String url) {
    URI uri = URI.create(url);
    String path = uri.getPath();
    return path.substring(path.lastIndexOf('/') + 1);
  }

}
