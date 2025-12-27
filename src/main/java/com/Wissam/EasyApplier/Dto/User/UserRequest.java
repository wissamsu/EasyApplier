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

  @Email
  @NotNull
  private String email;

  @NotNull
  private String password;

  @NotNull
  private UserRole role;

}
