package com.Wissam.EasyApplier.Dto.JobInfo;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobInfoRequest {

  @NotBlank(message = "Job name is required")
  private String jobName;

  @NotBlank(message = "Job location is required")
  private String jobLocation;

  @NotBlank(message = "Job ID is required")
  private String jobId;

  private Set<String> appliedUsers;

  @NotBlank(message = "Job URL is required")
  private String jobUrl;

  @NotBlank(message = "Job company name is required")
  private String jobCompanyName;

  @NotBlank(message = "Job company image link is required")
  private String jobCompanyImageLink;

}
