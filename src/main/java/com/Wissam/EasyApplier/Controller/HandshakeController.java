package com.Wissam.EasyApplier.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Dto.Handshake.HandshakeRequest;
import com.Wissam.EasyApplier.Dto.Handshake.HandshakeResponse;
import com.Wissam.EasyApplier.Services.HandshakeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Handshake", description = "Handshake operations")
@RequestMapping("/handshake")
public class HandshakeController {

  private final HandshakeService handshakeService;

  @GetMapping("/{id}")
  public HandshakeResponse getHandshakeById(Long id) {
    return handshakeService.getHandshakeById(id);
  }

  @GetMapping("/email/{email}")
  public HandshakeResponse getHandshakeByEmail(String email) {
    return handshakeService.getHandshakeByEmail(email);
  }

  @GetMapping("/user/{userId}")
  public HandshakeResponse createHandshakeByUserId(HandshakeRequest handshakeRequest, Long userId) {
    return handshakeService.createHandshakeByUserId(handshakeRequest, userId);
  }

}
