package com.Wissam.EasyApplier.Dto.JobInfo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobInfoResponse {

  private Long id;

  private String jobName;

  private String jobLocation;

  private String jobId;

  private String jobUrl;

  private String jobCompanyName;

  private String jobCompanyImageLink;

}
