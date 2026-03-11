package com.Wissam.EasyApplier.Extractions.Jsoup.Jobs;

import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Model.JobInfo;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.JobInfoRepository;
import com.Wissam.EasyApplier.Utils.HandshakeUtils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class HandshakeEasyJobExtractor {

  private final HandshakeUtils handshakeUtils;
  private final ApplicationEventPublisher publisher;
  private final JobInfoRepository jobInfoRepo;
  private ConcurrentHashMap<UUID, Object> locks = new ConcurrentHashMap<>();

  @Async
  public void jobsExtractor(String jobTitle, User user) {
    System.out.println("Starting job extraction for user " + user.getUuid());
    UUID userId = user.getUuid();
    Object lock = locks.computeIfAbsent(userId, id -> new Object());
    Path statePath = handshakeUtils.getContextPath(userId);

    synchronized (lock) {
      try (Playwright playwright = Playwright.create();
          Browser browser = playwright.chromium()
              .launch(new LaunchOptions().setHeadless(false).setSlowMo(300 + Math.random() * 1300));
          BrowserContext context = handshakeUtils.createOrLoadContext(statePath, browser);
          Page page = context.newPage();) {

        int lastPage = 1000;
        // Repeat for 100 pages
        for (int pageNumber = 1; pageNumber <= lastPage; pageNumber++) {
          System.out.println("Processing page: " + pageNumber);
          page.navigate("https://app.joinhandshake.com/job-search/?query="
              + URLEncoder.encode(jobTitle, "UTF-8")
              + "&per_page=50&page=" + pageNumber);
          page.waitForLoadState(LoadState.DOMCONTENTLOADED);

          // Handle login if needed
          if (page.url().equals("https://app.joinhandshake.com/login")) {
            handshakeUtils.oktaLogin(user, context, page);
            String expectedStart = "https://app.joinhandshake.com/job-search";
            page.waitForCondition(() -> page.url().startsWith(expectedStart),
                new Page.WaitForConditionOptions().setTimeout(100_000));
            // Reload page after login
            page.navigate("https://app.joinhandshake.com/job-search/?query="
                + URLEncoder.encode(jobTitle, "UTF-8")
                + "&per_page=50&page=" + pageNumber);
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
          }
          if (lastPage == 1000) {
            lastPage = Integer
                .parseInt(page.locator("nav[data-hook='job-search-pagination']").locator("ul").locator("li")
                    .locator("button").last().innerText());
            System.out.println("Last page: " + lastPage);
          }
          page.waitForTimeout(5000); // optional delay
          Document doc = Jsoup.parse(page.content());
          Elements jobCards = doc.select("div[data-hook^='job-result-card ']");

          for (Element jobCard : jobCards) {
            Element jobCardFooter = jobCard.selectFirst("div[data-hook='job-result-card-footer']");
            String jobLocation = jobCardFooter.selectFirst("span").text();
            String jobName = jobCard.selectFirst("a[role='button']").attr("aria-label");
            String jobId = handshakeUtils.extractJobId(
                "https://app.joinhandshake.com" + jobCard.selectFirst("a[role='button']").attr("href"));
            String jobLink = "https://app.joinhandshake.com/jobs/" + jobId;
            String companyName = jobCard.selectFirst("span").text();
            String companyImageLink = jobCard.select("img").attr("src");

            page.navigate(jobLink);
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);

            boolean alreadyApplied = page.getByText("Withdraw application").count() > 0
                || page.getByText("Your application has already been reviewed and cannot").count() > 0
                || page.getByText("To withdraw your application,").count() > 0
                || page.getByText("Did you apply to this job?").count() > 0
                || page.getByText("Applied").count() > 0;

            if (!alreadyApplied && page.locator("button[aria-label='Apply externally']").count() == 0) {
              JobInfo jobInfo = JobInfo.builder().jobId(jobId).jobUrl(jobLink).jobName(jobName).jobLocation(jobLocation)
                  .jobCompanyName(companyName)
                  .jobCompanyImageLink(companyImageLink).build();
              jobInfoRepo.save(jobInfo);
              System.out.println("Job saved: " + jobId);
            }
          }
        }

      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }
}
