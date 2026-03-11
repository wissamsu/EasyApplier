package com.Wissam.EasyApplier.Dto.JobInfo;

import java.util.Set;

import org.bson.types.ObjectId;

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

  private ObjectId id;

  private String jobName;

  private Set<String> appliedUsers;

  private String jobLocation;

  private String jobId;

  private String jobUrl;

  private String jobCompanyName;

  private String jobCompanyImageLink;

}
