package com.Wissam.EasyApplier.Controller.Handlers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Automations.Job.HiringCafe.HiringCafeAuto;
import com.Wissam.EasyApplier.Automations.Job.Linkedin.LinkedinAuto;
import com.Wissam.EasyApplier.Model.User;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/automations")
@Tag(name = "Automation", description = "General Automations")
public class GeneralAutomationController {

  private final LinkedinAuto linkedinAuto;
  private final HiringCafeAuto hiringCafeListener;

  @GetMapping("/jobAutomatorById/{jobId}")
  public void linkedinJobAutomatorById(@PathVariable String jobId, @AuthenticationPrincipal User user) {
    linkedinAuto.jobAutomatorById(jobId, user);
  }

  @GetMapping("/jobAutomator")
  public void linkedinJobAutomator(@AuthenticationPrincipal User user) {
    linkedinAuto.jobAutomator(user);
  }

  @GetMapping("/jobAutomatorByLink")
  public void cafeJobAutomatorByLink(@RequestParam String jobLink, @AuthenticationPrincipal User user) {
    hiringCafeListener.jobAutomatorById(user, jobLink);
  }

}
