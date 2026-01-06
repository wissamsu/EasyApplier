package com.Wissam.EasyApplier.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Extractions.Jsoup.Flights.Linkedin.LinkedinJobsExtractor;
import com.Wissam.EasyApplier.Model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/temp")
@RequiredArgsConstructor
public class ExtractorController {

  private final LinkedinJobsExtractor extractor;

  @GetMapping("/extractJobs/{jobTitle}")
  public void extractJobs(String jobTitle, @AuthenticationPrincipal User user) {
    extractor.jobsExtractor(jobTitle, user);
  }

}
