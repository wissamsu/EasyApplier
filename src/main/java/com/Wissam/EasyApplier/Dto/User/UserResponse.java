package com.Wissam.EasyApplier.Dto.User;

import java.time.LocalDateTime;

import com.Wissam.EasyApplier.Enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

  @NotNull
  private Long id;

  @Email
  @NotNull
  private String email;

  @NotNull
  private UserRole role;

  @NotNull
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

}
