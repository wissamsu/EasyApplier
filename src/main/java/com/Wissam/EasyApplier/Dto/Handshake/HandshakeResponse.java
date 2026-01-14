package com.Wissam.EasyApplier.Dto.Handshake;

import com.Wissam.EasyApplier.Model.User;
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
public class HandshakeResponse {

  private Long id;

  private String email;

  private User user;

}
