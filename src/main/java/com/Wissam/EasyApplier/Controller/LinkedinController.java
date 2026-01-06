package com.Wissam.EasyApplier.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Automations.Linkedin.LinkedinAuto;
import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinRequest;
import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinResponse;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Services.IServices.ILinkedinService;
import com.Wissam.EasyApplier.Utils.LinkedinUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/linkedin")
@RequiredArgsConstructor
@Tag(name = "Linkedin", description = "Linkedin endpoints")
public class LinkedinController {

  private final ILinkedinService linkedinService;
  private final LinkedinUtils linkedinUtils;
  private final LinkedinAuto linkedin;

  @GetMapping("/{id}")
  @Operation(summary = "Get Linkedin by id")
  public ResponseEntity<LinkedinResponse> getLinkedinById(Long id) {
    return ResponseEntity.ok(linkedinService.getLinkedinById(id));
  }

  @GetMapping("/email/{email}")
  @Operation(summary = "Get Linkedin by email")
  public ResponseEntity<LinkedinResponse> getLinkedinByEmail(String email) {
    return ResponseEntity.ok(linkedinService.getLinkedinByEmail(email));
  }

  @PostMapping("/user/{userId}")
  @Operation(summary = "Create Linkedin by user id")
  public ResponseEntity<LinkedinResponse> createLinkedinByUserId(LinkedinRequest linkedinRequest, Long userId) {
    return ResponseEntity.ok(linkedinService.createLinkedinByUserId(linkedinRequest, userId));
  }

  @GetMapping("/cookie")
  @Operation(summary = "Check or get Linkedin Li_at cookie")
  public ResponseEntity<Void> checkOrgetLiAtCookie(@AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(null);
  }

  @PutMapping("/cookie/{liAtCookie}")
  public LinkedinResponse addLi_AtCookie(@AuthenticationPrincipal User user, @PathVariable String liAtCookie) {
    return linkedinService.addLi_AtCookie(user, liAtCookie);
  }

}
