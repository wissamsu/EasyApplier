package com.Wissam.EasyApplier.Dto.Handshake;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
  private String email;

  @NotEmpty(message = "Password must not be empty")
  private String password;

}
