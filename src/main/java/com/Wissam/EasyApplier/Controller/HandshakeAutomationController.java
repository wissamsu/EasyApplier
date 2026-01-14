package com.Wissam.EasyApplier.Controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Extractions.Jsoup.Jobs.HandshakeEasyJobExtractor;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.ObjectReturns.job.HandshakeEasyJobInfo;
import com.Wissam.EasyApplier.Utils.HandshakeUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/handshake")
public class HandshakeAutomationController {

  // private final HandshakeAuto handshakeAuto;
  private final HandshakeUtils handshakeUtils;
  private final HandshakeEasyJobExtractor handshakeEasyJobExtractor;

  @GetMapping("/extractJobs/{jobTitle}/{pageNumber}")
  public CompletableFuture<List<HandshakeEasyJobInfo>> jobsExtractor(@PathVariable String jobTitle,
      @AuthenticationPrincipal User user,
      @PathVariable int pageNumber) {
    return handshakeEasyJobExtractor.jobsExtractor(jobTitle, user, pageNumber);
  }

  @GetMapping("/login")
  public void oktaLogin(@AuthenticationPrincipal User user) {
    handshakeUtils.oktaLogin(user);
  }

}
