package com.Wissam.EasyApplier.Dto.Linkedin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkedinRequest {

  @Email(message = "email is not valid")
  @NotBlank(message = "email is required")
  private String email;

  @Min(value = 8, message = "password must be at least 8 characters")
  @NotBlank(message = "password is required")
  private String password;

}
