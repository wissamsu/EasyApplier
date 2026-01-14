package com.Wissam.EasyApplier.Extractions.Jsoup.Jobs;

import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.ObjectReturns.job.LinkedinEasyJobInfo;
import com.Wissam.EasyApplier.Utils.LinkedinUtils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.Cookie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class GeneralJobExtractions {

  private final LinkedinUtils linkedinUtils;
  private final Browser browser;
  private ConcurrentHashMap<UUID, Object> locks = new ConcurrentHashMap<>();

  @Async
  public void jobsExtractor(String jobTitle, User user) {
    UUID userId = UUID.fromString(user.getUuid());
    Object lock = locks.computeIfAbsent(userId, id -> new Object());
    synchronized (lock) {
      Path statePath = linkedinUtils.getContextPath(userId);
      // first 25 jobs
      try (BrowserContext context = linkedinUtils.createOrLoadContext(statePath, browser);
          Page page = context.newPage();) {

        String li_at = linkedinUtils.checkOrgetLiAtCookie((UserDetails) user);
        Cookie cookie = new Cookie("li_at", li_at);
        cookie.setUrl("https://www.linkedin.com");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        context.addCookies(List.of(cookie));
        // Page page = context.newPage();
        page.navigate("https://www.linkedin.com/jobs/search/?keywords=" + URLEncoder.encode(jobTitle, "UTF-8")
            + "&f_AL=true&keywords=java&origin=JOB_SEARCH_PAGE_JOB_FILTER&sortBy=R");

        Document doc = Jsoup.parse(page.content());

        Elements jobCards = doc.select("li[data-occludable-job-id]");

        int i = 1;

        List<LinkedinEasyJobInfo> jobInfos = new ArrayList<>();
        for (Element jobCard : jobCards) {

          boolean isApplied = jobCard
              .select("li.job-card-container__footer-job-state")
              .stream()
              .anyMatch(e -> e.text().equalsIgnoreCase("Applied"));

          if (isApplied) {
            continue;
          }

          String jobId = jobCard.attr("data-occludable-job-id");

          log.info("--------------------------------------------------------------------------");
          log.info("Extracting job {} jobId={}", i++, jobId);
          LinkedinEasyJobInfo jobInfo = jobInfoExtractor(page, jobId, context, user);
          jobInfos.add(jobInfo);

        }

        // next 25 jobs
        // Page page2 = context.newPage();
        page.navigate("https://www.linkedin.com/jobs/search/?keywords=" + URLEncoder.encode(jobTitle, "UTF-8")
            + "&f_AL=true&keywords=java&origin=JOB_SEARCH_PAGE_JOB_FILTER&sortBy=R");

        Document doc2 = Jsoup.parse(page.content());
        // page.close();

        Elements jobCards2 = doc2.select("li[data-occludable-job-id]");

        for (Element jobCard : jobCards2) {

          boolean isApplied = jobCard
              .select("li.job-card-container__footer-job-state")
              .stream()
              .anyMatch(e -> e.text().equalsIgnoreCase("Applied"));

          if (isApplied) {
            continue;
          }

          String jobId = jobCard.attr("data-occludable-job-id");

          log.info("--------------------------------------------------------------------------");
          log.info("Extracting job {} jobId={}", i++, jobId);
          LinkedinEasyJobInfo jobInfo = jobInfoExtractor(page, jobId, context, user);

          jobInfos.add(jobInfo);

        }
        linkedinUtils.saveContext(context, statePath);
      } catch (Exception e) {
        log.error("Error while extracting jobs method jobsExtractor in class LinkedinJobsExtractor: " + e.getMessage());
      }
    }
  }

  public LinkedinEasyJobInfo jobInfoExtractor(Page page, String jobId, BrowserContext context, User user) {
    try {
      // Page page = context.newPage();
      String jobLink = "https://www.linkedin.com/jobs/view/" + jobId;
      page.navigate(jobLink);

      Document doc = Jsoup.parse(page.content());
      // page.close();

      String jobTitle = doc.select("p").get(2).text();
      log.info("Job title: " + jobTitle);

      log.info("Job link: " + jobLink);

      String imgLink = doc.select("img").get(1).attr("src");
      log.info("Job image link: " + imgLink);

      String companyName = doc.select("p:has(a)").get(0).text();
      log.info("Company name: " + companyName);

      String actualJobLocation;
      Element jobLocationElement = doc.select("p:has(span)").get(1);
      Element jobLocation = jobLocationElement.selectFirst("span");
      if (jobLocation.childNodeSize() > 6) {
        log.info("Job location: " + jobLocationElement.text());
        log.info("Used first");
        actualJobLocation = jobLocation.text();
      }

      else {
        Element jobLocationElement1 = doc.select("p:has(span)").get(0);
        Element jobLocation1 = jobLocationElement1.selectFirst("span");
        log.info("Job location: " + jobLocation1.text());
        log.info("Used second");
        actualJobLocation = jobLocation1.text();
      }

      String jobDescription = doc.select("span:has(p)").text();
      log.info("Job description: " + jobDescription);
      // TODO: add job description

      log.info("--------------------------------------------------------------------------");
      return new LinkedinEasyJobInfo(jobId, jobTitle, imgLink, jobLink, actualJobLocation, companyName, user);
    } catch (Exception e) {
      log.error("Error while extracting job info method jobInfoExtractor in class LinkedinJobsExtractor: "
          + e.getMessage());
      return null;
    }
  }

}
