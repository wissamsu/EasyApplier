package com.Wissam.EasyApplier.Listeners.Jobs;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.ObjectReturns.job.HandshakeEasyJobInfo;
import com.Wissam.EasyApplier.Utils.HandshakeUtils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.FilePayload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HandshakeListener {

  private final ConcurrentHashMap<UUID, Object> locks = new ConcurrentHashMap<>();
  private final Browser browser;
  private final HandshakeUtils handshakeUtils;

  @EventListener
  @Async
  public void onJobFoundEvent(HandshakeEasyJobInfo jobInfo) {
    UUID uuid = jobInfo.user().getUuid();
    Object lock = locks.computeIfAbsent(uuid, id -> new Object());
    Path statePath = handshakeUtils.getContextPath(uuid);
    synchronized (lock) {
      try (BrowserContext ctx = handshakeUtils.createOrLoadContext(statePath, browser); Page page = ctx.newPage();) {
        page.navigate(jobInfo.jobLink());

        if (page.locator("button[aria-label='Apply externally']").count() > 0) {

        } else if (page.getByText("Withdraw application").count() > 0) {
        } else if (page.getByText("Your application has already been reviewed and cannot").count() > 0) {
        } else if (page.getByText("To withdraw your application,").count() > 0) {
        } else if (page.getByText("Did you apply to this job?").count() > 0) {
        } else {
          Locator div = page.locator("div[data-hook='apply-modal-content']");
          page.locator("button[aria-label='Apply']").first().click();
          div.waitFor();
          if (page.locator("input").count() > 0) {
            if (page.locator("input[name='phone']").count() > 0) {
              page.locator("input[name='phone']").fill(jobInfo.user().getPhoneNumber());
            }
          }
          HttpClient client = HttpClient.newHttpClient();

          HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(
                  jobInfo.user().getResumeLink()))
              .GET()
              .build();

          byte[] fileBytes = client
              .send(request, HttpResponse.BodyHandlers.ofByteArray())
              .body();

          FilePayload payload = new FilePayload(
              "WissamResume.pdf",
              "application/pdf",
              fileBytes);

          if (div.locator("input[type='file']").count() > 0) {
            div.locator("input[type='file']").nth(0).setInputFiles(payload);
            page.waitForTimeout(5000);
          }
          if (div.locator("input[type='file']").count() > 0) {
            div.locator("input[type='file']").nth(1).setInputFiles(payload);
            page.waitForTimeout(5000);
          }
          if (div.getByText("Submit application").count() > 0) {
            div.getByText("Submit application").click();
            page.waitForTimeout(1000);
          }

          while (div.getByText("Submit application").isDisabled()) {
            page.waitForTimeout(1000);
          }
          if (div.getByText("Submit application").count() > 0 && div.getByText("Submit application").isVisible()
              && div.getByText("Submit application").isEnabled()) {
            div.getByText("Submit application").click();
          }
        }
      } catch (Exception e) {
        log.error("Error: " + e.getMessage());
      }
    }
  }

}
