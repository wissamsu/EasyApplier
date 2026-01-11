package com.Wissam.EasyApplier.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Extractions.Jsoup.Jobs.GeneralJobExtractions;
import com.Wissam.EasyApplier.Model.User;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/general")
@Tag(name = "General", description = "General Automations")
public class GeneralAutomationsController {

  private final GeneralJobExtractions jobsExtractor;

  @GetMapping("/extractJobs/{jobTitle}")
  public void extractJobs(String jobTitle, @AuthenticationPrincipal User user) {
    jobsExtractor.jobsExtractor(jobTitle, user);
  }

}
