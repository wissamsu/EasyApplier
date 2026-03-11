package com.Wissam.EasyApplier.Automations.Job.Handshake;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Dto.JobInfo.JobInfoResponse;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Utils.GeneralUtils;
import com.Wissam.EasyApplier.Utils.HandshakeUtils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.FilePayload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HandshakeAuto {

  private final ConcurrentHashMap<UUID, Object> locks = new ConcurrentHashMap<>();
  private final HandshakeUtils handshakeUtils;
  private final GeneralUtils generalUtils;

  @Async
  public void onJobFoundEvent2(JobInfoResponse jobInfo, User user) {
    UUID uuid = user.getUuid();
    Object lock = locks.computeIfAbsent(uuid, id -> new Object());
    Path statePath = handshakeUtils.getContextPath(uuid);

    synchronized (lock) {
      try (Playwright playwright = Playwright.create();
          Browser browser = playwright.chromium()
              .launch(new LaunchOptions().setHeadless(false).setSlowMo(300 + Math.random() * 1300));
          BrowserContext ctx = handshakeUtils.createOrLoadContext(statePath, browser);
          Page page = ctx.newPage();) {

        Document doc = Jsoup.connect(jobInfo.getJobUrl()).cookies(generalUtils.getCookiesFromBrowserContext(ctx)).get();
        if (doc.select("button[aria-label='Apply externally']").size() > 0) {
          System.out.println("Found button count is " + doc.select("button[aria-label='Apply externally']").size());
          return;
        } else {
          page.navigate(jobInfo.getJobUrl());

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
                page.locator("input[name='phone']").fill(user.getPhoneNumber());
              }
            }
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                    user.getResumeLink()))
                .GET()
                .build();

            byte[] fileBytes = client
                .send(request, HttpResponse.BodyHandlers.ofByteArray())
                .body();

            FilePayload payload = new FilePayload(
                "WissamResume.pdf",
                "application/pdf",
                fileBytes);

            for (int i = 0; i < div.locator("input[type='file']").count(); i++) {
              System.out.println(div.locator("input[type='file']").count());
              div.locator("input[type='file']").nth(i).setInputFiles(payload);
              page.waitForTimeout(5000);
            }
            if (div.getByText("Submit application").count() > 0) {
              div.getByText("Submit application").click();
              page.waitForTimeout(1000);
            }

            int time = 0;
            while (div.getByText("Submit application").isDisabled()) {
              page.waitForTimeout(1000);
              time++;
              if (time > 15) {
                break;
              }
              System.out.println("Waiting for submit button to be enabled");
            }
            if (div.getByText("Submit application").count() > 0 && div.getByText("Submit application").isVisible()
                && div.getByText("Submit application").isEnabled()) {
              div.getByText("Submit application").click();
              System.out.println("Submitted application");
            }
          }
        }
      } catch (Exception e) {
        log.error("Error: " + e.getMessage());
      }
    }
  }

}
