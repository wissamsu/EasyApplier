package com.Wissam.EasyApplier.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Dto.Handshake.HandshakeRequest;
import com.Wissam.EasyApplier.Dto.Handshake.HandshakeResponse;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Services.HandshakeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Handshake", description = "Handshake operations")
@RequestMapping("/handshake")
public class HandshakeController {

  private final HandshakeService handshakeService;

  @GetMapping("/get")
  @Operation(summary = "Get handshake")
  public HandshakeResponse getHandshakeById(@AuthenticationPrincipal User user) {
    return handshakeService.getHandshake(user);
  }

  @GetMapping("/email/{email}")
  @Operation(summary = "Get handshake by email")
  public HandshakeResponse getHandshakeByEmail(String email) {
    return handshakeService.getHandshakeByEmail(email);
  }

  @PostMapping("/user")
  @Operation(summary = "Get handshake by user id")
  public HandshakeResponse createHandshake(HandshakeRequest handshakeRequest,
      @AuthenticationPrincipal User user) {
    return handshakeService.createHandshake(handshakeRequest, user);
  }

  @PutMapping("/update")
  @Operation(summary = "Update handshake")
  public HandshakeResponse updateHandshake(@RequestBody HandshakeRequest handshakeRequest,
      @AuthenticationPrincipal User user) {
    return handshakeService.updateHandshake(handshakeRequest, user);
  }

}
