package com.Wissam.EasyApplier.Extractions.Jsoup.Jobs;

import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.ObjectReturns.job.HandshakeEasyJobInfo;
import com.Wissam.EasyApplier.Utils.HandshakeUtils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class HandshakeEasyJobExtractor {

  private final Browser browser;
  private final HandshakeUtils handshakeUtils;
  private final ApplicationEventPublisher publisher;
  private ConcurrentHashMap<UUID, Object> locks = new ConcurrentHashMap<>();

  @Async
  public CompletableFuture<List<HandshakeEasyJobInfo>> jobsExtractor(String jobTitle, User user, int pageNumber) {
    UUID userId = UUID.fromString(user.getUuid());
    Object lock = locks.computeIfAbsent(userId, id -> new Object());
    Path statePath = handshakeUtils.getContextPath(userId);
    synchronized (lock) {
      List<HandshakeEasyJobInfo> jobInfos = new ArrayList<>();
      // first 25 jobs
      try (BrowserContext context = handshakeUtils.createOrLoadContext(statePath, browser);
          Page page = context.newPage();) {
        page.navigate("https://app.joinhandshake.com/job-search/?query=" + URLEncoder.encode(jobTitle, "UTF-8")
            + "&per_page=25&page=" + pageNumber);
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        // page.waitForTimeout(5000);
        Document doc = Jsoup.parse(page.content());
        Elements jobCards = doc.select("div[data-hook^='job-result-card ']");
        for (Element jobCard : jobCards) {
          // JobLocation
          Element jobCardFooter = jobCard.selectFirst("div[data-hook='job-result-card-footer']");
          log.info("----------------------------------------------------");
          String jobLocation = jobCardFooter.selectFirst("span").text();
          log.info("Job location: " + jobLocation);
          // JobName
          String jobName = jobCard.selectFirst("a[role='button']").attr("aria-label");
          log.info("Job name: " + jobName);
          // JobLink
          String jobId = handshakeUtils
              .extractJobId("https://app.joinhandshake.com" + jobCard.selectFirst("a[role='button']").attr("href"));
          log.info("Job Id: " + jobId);
          // JobLink
          String jobLink = "https://app.joinhandshake.com/jobs/" + jobId;
          log.info("Job link: " + jobLink);
          // CompanyName
          String companyName = jobCard.selectFirst("span").text();
          log.info("Company name: " + companyName);
          // CompanyImageLink
          String companyImageLink = jobCard.select("img").attr("src");
          log.info("Company image link: " + companyImageLink);
          jobInfos
              .add(new HandshakeEasyJobInfo(jobId, jobName, companyImageLink, jobLink, jobLocation, companyName, user));
          publisher.publishEvent(
              new HandshakeEasyJobInfo(jobId, jobName, companyImageLink, jobLink, jobLocation, companyName, user));

        }
        return CompletableFuture.completedFuture(jobInfos);

      } catch (Exception e) {
        log.error(e.getMessage());
        return null;
      }
    }
  }

}
