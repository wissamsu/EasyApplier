package com.Wissam.EasyApplier.Dto.JobAnswer;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobAnswerResponse {

  private Long id;

  private String address;

  private String city;

  private String state;

  private String zipCode;

  private String country;

  private String linkedinProfileUrl;

  private Short yearsOfExperience;

  private Boolean eligibleToWorkInUS;

  private Boolean requireVisa;

}
