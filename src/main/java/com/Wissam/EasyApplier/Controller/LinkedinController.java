package com.Wissam.EasyApplier.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinRequest;
import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinResponse;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Services.IServices.ILinkedinService;
import com.Wissam.EasyApplier.Utils.LinkedinUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/linkedin")
@RequiredArgsConstructor
@Tag(name = "Linkedin", description = "Linkedin endpoints")
@Validated
public class LinkedinController {

  private final ILinkedinService linkedinService;
  private final LinkedinUtils linkedinUtils;

  @GetMapping("/me")
  @Operation(summary = "Get current user's Linkedin profile")
  public ResponseEntity<LinkedinResponse> getLinkedin(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(linkedinService.getLinkedin(user));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get Linkedin by id")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<LinkedinResponse> getLinkedinById(@PathVariable Long id) {
    return ResponseEntity.ok(linkedinService.getLinkedinById(id));
  }

  @GetMapping("/email/{email}")
  @Operation(summary = "Get Linkedin by email")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<LinkedinResponse> getLinkedinByEmail(@PathVariable String email) {
    return ResponseEntity.ok(linkedinService.getLinkedinByEmail(email));
  }

  @PostMapping("/user")
  @Operation(summary = "Create Linkedin")
  public ResponseEntity<LinkedinResponse> createLinkedinByUserId(@Valid @RequestBody LinkedinRequest linkedinRequest,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(linkedinService.createLinkedin(linkedinRequest, user));
  }

  @GetMapping("/cookie")
  @Operation(summary = "Check or get Linkedin Li_at cookie")
  public ResponseEntity<String> checkOrgetLiAtCookie(@AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(linkedinUtils.checkOrgetLiAtCookie(userDetails));
  }

  @PutMapping("/cookie")
  public LinkedinResponse addLi_AtCookie(@AuthenticationPrincipal User user, @RequestBody String liAtCookie) {
    return linkedinService.addLi_AtCookie(user, liAtCookie);
  }

}
