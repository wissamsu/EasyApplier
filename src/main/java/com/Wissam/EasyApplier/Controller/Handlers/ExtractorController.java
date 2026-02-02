package com.Wissam.EasyApplier.Controller.Handlers;

import java.time.Duration;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Extractions.Jsoup.Jobs.GeneralJobExtractions;
import com.Wissam.EasyApplier.Extractions.Jsoup.Jobs.HandshakeEasyJobExtractor;
import com.Wissam.EasyApplier.Extractions.Jsoup.Jobs.HiringCafeJobExtractor;
import com.Wissam.EasyApplier.Extractions.Jsoup.Jobs.LinkedinEasyJobsExtractor;
import com.Wissam.EasyApplier.Model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExtractorController {

  private final LinkedinEasyJobsExtractor extractor;
  private final HiringCafeJobExtractor jobExtractor;
  private final HandshakeEasyJobExtractor handshakeEasyJobExtractor;
  private final GeneralJobExtractions jobsExtractor;

  @GetMapping("/extractLinkedinJobs/{jobTitle}")
  public void extractLinkedinJobs(String jobTitle, @AuthenticationPrincipal User user) {
    extractor.jobsExtractor(jobTitle, user);
  }

  @GetMapping("/extractCafeJobs/{jobTitle}")
  public void extractCafeJobs(@PathVariable String jobTitle, @AuthenticationPrincipal User user) {
    jobExtractor.jobExtractor(user, jobTitle);
  }

  @GetMapping("/extractHandshakeJobs/{jobTitle}")
  public void extractHandshakeJobs(@PathVariable String jobTitle,
      @AuthenticationPrincipal User user) throws InterruptedException {
    for (int i = 0; i < 50; i++) {
      handshakeEasyJobExtractor.jobsExtractor(jobTitle, user, i);
      Thread.sleep(Duration.ofMinutes(30));
    }
  }

  @GetMapping("/extractGeneralJobs/{jobTitle}")
  public void extractGeneralJobs(String jobTitle, @AuthenticationPrincipal User user) {
    jobsExtractor.jobsExtractor(jobTitle, user);
  }

}
