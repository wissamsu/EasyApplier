package com.Wissam.EasyApplier.Dto.Linkedin;

import com.Wissam.EasyApplier.Model.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkedinRequest {

  @Min(value = 15, message = "liat cookie must be at least 8 characters")
  private String liatCookie;

  @Email
  private String email;

  @Min(value = 8, message = "password must be at least 8 characters")
  private String password;

  @NotNull(message = "user is required")
  @Schema(hidden = true)
  private User user;

}
