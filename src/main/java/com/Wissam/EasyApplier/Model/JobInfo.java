package com.Wissam.EasyApplier.Model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
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
  private ObjectId id;

  private String jobName;

  private String jobLocation;

  @Default
  private Set<String> appliedUsers = new HashSet<>();

  private String jobId;

  private String jobUrl;

  private String jobCompanyName;

  private String jobCompanyImageLink;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

}
