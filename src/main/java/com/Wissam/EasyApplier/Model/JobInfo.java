package com.Wissam.EasyApplier.Model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "jobInfo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobInfo {

  @Id
  private String id;

  private String jobName;

  private String jobLocation;

  private Set<String> appliedUsers;

  private String jobId;

  private String jobUrl;

  private String jobCompanyName;

  private String jobCompanyImageLink;

}
