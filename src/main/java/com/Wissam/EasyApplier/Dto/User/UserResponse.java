package com.Wissam.EasyApplier.Dto.User;

import java.time.LocalDateTime;

import com.Wissam.EasyApplier.Enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

  @NotNull(message = "Id is required")
  @Positive(message = "Id must be positive")
  private Long id;

  @Email(message = "Email is not valid")
  @NotNull(message = "Email is required")
  private String email;

  private UserRole role;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

}
