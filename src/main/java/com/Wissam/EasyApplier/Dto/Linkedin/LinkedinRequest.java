package com.Wissam.EasyApplier.Dto.Linkedin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

  @Size(min = 8, message = "password must be at least 8 characters")
  @NotBlank(message = "password is required")
  private String password;

}
