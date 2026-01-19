package com.Wissam.EasyApplier.Extractions.Jsoup.Jobs;

import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.ObjectReturns.job.HiringCafeJobInfo;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HiringCafeJobExtractor {

  private final Browser browser;
  private ConcurrentHashMap<UUID, Object> locks = new ConcurrentHashMap<>();
  private final ApplicationEventPublisher publisher;

  @Async
  public void jobExtractor(User user, String jobTitle) {
    UUID uuid = user.getUuid();
    Object lock = locks.computeIfAbsent(uuid, id -> new Object());
    synchronized (lock) {
      try (Page page = browser.newPage();) {
        page.navigate(
            "Https://hiring.cafe/?searchState=%7B%22applicationFormEase%22%3A%5B%22Simple%22%5D%2C%22searchQuery%22%3A%22"
                + URLEncoder.encode(jobTitle, "UTF-8") + "%22%7D");
        page.waitForTimeout(5000);
        page.evaluate("height => window.scrollTo(0, height)", (int) page.evaluate("() => document.body.scrollHeight"));
        page.waitForTimeout(1000);
        page.evaluate("height => window.scrollTo(0, height)", (int) page.evaluate("() => document.body.scrollHeight"));
        page.waitForTimeout(1000);
        page.evaluate("height => window.scrollTo(0, height)", (int) page.evaluate("() => document.body.scrollHeight"));
        page.waitForTimeout(1000);
        Document doc = Jsoup.parse(page.content());
        Elements jobDivs = doc.select(".md\\:hover\\:border-gray-200");

        for (Element jobDiv : jobDivs) {
          log.info("-------------------------------------------------------------");
          String jobLink = jobDiv.select("a").attr("href");
          log.info("Job link: " + jobLink);
          String jobId = jobLink.replace("https://hiring.cafe/viewjob/", "");
          log.info("Job id: " + jobId);
          String jobName = jobDiv.select(".text-start.line-clamp-2").text();
          log.info("Job name: " + jobName);
          String jobImageLink = jobDiv.select(".object-contain").attr("src");
          log.info("Job image link: " + jobImageLink);
          String jobLocation = jobDiv.select("span[class='line-clamp-2']").text();
          log.info("Job location: " + jobLocation);
          String companyName = jobDiv.select(".line-clamp-3 .font-bold").text();
          log.info("Company name: " + companyName);
          publisher.publishEvent(
              new HiringCafeJobInfo(jobId, jobName, jobImageLink, jobLink, jobLocation, companyName, user));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
