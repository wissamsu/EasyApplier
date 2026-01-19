package com.Wissam.EasyApplier.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Dto.JobAnswer.JobAnswerRequest;
import com.Wissam.EasyApplier.Dto.JobAnswer.JobAnswerResponse;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Services.JobAnswerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobAnswer")
@Tag(name = "JobAnswer")
public class JobAnswerController {

  private final JobAnswerService jobAnswerService;

  @GetMapping("/getJobAnswers")
  @Operation(summary = "Get job answers")
  public JobAnswerResponse getJobAnswers(@AuthenticationPrincipal User user) {
    return jobAnswerService.getJobAnswers(user);
  }

  @PostMapping("/saveJobAnswers")
  @Operation(summary = "Save job answers")
  public JobAnswerResponse saveJobAnswers(@AuthenticationPrincipal User user,
      @RequestBody JobAnswerRequest jobAnswerRequest) {
    return jobAnswerService.saveJobAnswers(user, jobAnswerRequest);
  }

  @PutMapping("/updateJobAnswers")
  @Operation(summary = "Update job answers")
  public JobAnswerResponse updateJobAnswers(@AuthenticationPrincipal User user,
      @RequestBody JobAnswerRequest jobAnswerRequest) {
    return jobAnswerService.updateJobAnswers(user, jobAnswerRequest);
  }

}
