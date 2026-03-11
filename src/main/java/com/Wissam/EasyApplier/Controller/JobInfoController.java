package com.Wissam.EasyApplier.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Dto.JobInfo.JobInfoResponse;
import com.Wissam.EasyApplier.Services.JobInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jobInfo")
@CrossOrigin("*")
@Tag(name = "JobInfo")
@RequiredArgsConstructor
public class JobInfoController {

  private final JobInfoService jobInfoService;

  @GetMapping("/all/{jobLocation}")
  @Operation(summary = "Get all job info's by job location")
  public List<JobInfoResponse> findAllByJobLocation(@PathVariable String jobLocation) {
    return jobInfoService.findAllByJobLocation(jobLocation);
  }

  @GetMapping("/jobCompanyName/{jobCompanyName}")
  @Operation(summary = "Get all job info's by job company name")
  public List<JobInfoResponse> findAllByJobCompanyName(@PathVariable String jobCompanyName) {
    return jobInfoService.findAllByJobCompanyName(jobCompanyName);
  }

  @GetMapping("/jobName/{jobName}")
  @Operation(summary = "Get all job info's by job name")
  public List<JobInfoResponse> findAllByJobName(@PathVariable String jobName) {
    return jobInfoService.findAllByJobName(jobName);
  }

  @GetMapping("/all")
  @Operation(summary = "Get all job infos")
  public List<JobInfoResponse> findAll() {
    return jobInfoService.findAll();
  }

}
