package com.Wissam.EasyApplier.Dto.User;

import java.time.LocalDateTime;
import java.util.UUID;

import com.Wissam.EasyApplier.Dto.Handshake.HandshakeResponse;
import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinResponse;
import com.Wissam.EasyApplier.Enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;

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

  private Long id;

  private String email;

  private String resumeLink;

  private String firstName;

  private String lastName;

  private String phoneNumber;

  private boolean verified;

  private UserRole role;

  private UUID uuid;

  private LinkedinResponse linkedin;
  private HandshakeResponse handshake;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

}
