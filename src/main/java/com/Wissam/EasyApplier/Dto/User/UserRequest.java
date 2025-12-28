package com.Wissam.EasyApplier.Dto.User;

import com.Wissam.EasyApplier.Enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

  @Email(message = "Email is not valid")
  @NotNull(message = "Email is required")
  private String email;

  @NotNull(message = "Password is required")
  private String password;

  private UserRole role;

}
