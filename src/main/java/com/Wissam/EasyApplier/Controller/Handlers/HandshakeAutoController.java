package com.Wissam.EasyApplier.Controller.Handlers;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Automations.Job.Handshake.HandshakeAuto;
import com.Wissam.EasyApplier.Dto.JobInfo.JobInfoResponse;
import com.Wissam.EasyApplier.Mapper.JobInfoMapper;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Services.JobInfoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequestMapping("/handshakeAuto")
@RequiredArgsConstructor
@Tag(name = "Handshake Automation")
public class HandshakeAutoController {

  private final HandshakeAuto handshakeAuto;
  private final JobInfoService jobInfoService;
  private final JobInfoMapper jobInfoMapper;

  @GetMapping("/applyToAllSavedJobs")
  public void applyToAllSavedJobs(@AuthenticationPrincipal User user) {
    List<JobInfoResponse> jobInfos = jobInfoService.findAll();
    for (JobInfoResponse jobInfo : jobInfos) {
      // if (jobInfo.getAppliedUsers().contains(user.getId().toString())) {
      // continue;
      // }
      handshakeAuto.onJobFoundEvent2(jobInfo, user);
      jobInfo.getAppliedUsers().add(user.getId().toString());
      jobInfoService.save(jobInfoMapper.toJobInfo(jobInfo));
    }
  }

}
