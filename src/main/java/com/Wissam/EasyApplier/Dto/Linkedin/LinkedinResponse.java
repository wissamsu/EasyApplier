package com.Wissam.EasyApplier.Dto.Linkedin;

import com.Wissam.EasyApplier.Model.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkedinResponse {

  @NotNull(message = "id is required")
  @Positive(message = "id must be positive")
  private Long id;

  @Min(value = 15, message = "liat cookie must be at least 8 characters")
  private String liatCookie;

  @Email
  private String email;

  @Min(value = 8, message = "password must be at least 8 characters")
  private String password;

  @NotNull(message = "user is required")
  private User user;

}
