package com.Wissam.EasyApplier.Dto.Handshake;

import com.Wissam.EasyApplier.Model.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HandshakeRequest {

  @Email(message = "Email is not valid")
  @NotEmpty(message = "Email must not be empty")
  @NotNull(message = "Email must not be null")
  private String email;

  @NotEmpty(message = "Password must not be empty")
  @NotNull(message = "Password must not be null")
  private String password;

  @Schema(hidden = true)
  private User user;

}
