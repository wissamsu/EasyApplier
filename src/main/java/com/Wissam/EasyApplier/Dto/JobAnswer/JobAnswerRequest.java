package com.Wissam.EasyApplier.Dto.JobAnswer;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobAnswerRequest {

  @NotBlank(message = "Question cannot be empty")
  private String address;

  @NotBlank(message = "Answer cannot be empty")
  private String city;

  @NotBlank(message = "Answer cannot be empty")
  private String state;

  @NotBlank(message = "Answer cannot be empty")
  private String zipCode;

  @NotBlank(message = "Answer cannot be empty")
  private String country;

  @NotBlank(message = "Answer cannot be empty")
  private String linkedinProfileUrl;

  @NotNull(message = "Answer cannot be empty")
  @Positive(message = "Years of experience must be positive")
  @Max(value = 100, message = "Years of experience must be less than 100")
  private Short yearsOfExperience;

  @NotNull(message = "Answer cannot be empty")
  private Boolean eligibleToWorkInUS;

  @NotNull(message = "Answer cannot be empty")
  private Boolean requireVisa;
}
