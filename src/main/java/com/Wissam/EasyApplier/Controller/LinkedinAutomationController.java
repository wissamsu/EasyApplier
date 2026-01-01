package com.Wissam.EasyApplier.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Automations.Linkedin.LinkedinAuto;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/linkedinAuto")
@Tag(name = "Linkedin Automation", description = "Linkedin Automation")
public class LinkedinAutomationController {

  private final LinkedinAuto linkedinAuto;

  @GetMapping("/login")
  public void login(@AuthenticationPrincipal UserDetails userDetails) {
    linkedinAuto.login(userDetails);
  }

}
