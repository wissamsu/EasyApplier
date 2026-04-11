package com.Wissam.EasyApplier.Controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Wissam.EasyApplier.Dto.User.UserRequest;
import com.Wissam.EasyApplier.Dto.User.UserResponse;
import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Services.IServices.IUserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User")
@Validated
public class UserController {

  private final IUserService userService;

  @GetMapping("/me")
  public UserResponse getCurrentUser(@AuthenticationPrincipal User user) {
    return userService.getCurrentUser(user);
  }

  @GetMapping("/email/{email}")
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse findUserByEmail(@PathVariable String email) {
    return userService.findUserByEmail(email);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse findUserById(@PathVariable Long id) {
    return userService.findUserById(id);
  }

  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserResponse> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/role/{role}")
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserResponse> getAllUsersByRole(@PathVariable UserRole role) {
    return userService.getAllUsersByRole(role);
  }

  @GetMapping("/linkedinId/{linkedinId}")
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse findUserByLinkedinId(@PathVariable Long linkedinId) {
    return userService.findUserByLinkedinId(linkedinId);
  }

  @GetMapping("/linkedinEmail/{linkedinEmail}")
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse findUserByLinkedinEmail(@PathVariable String linkedinEmail) {
    return userService.findUserByLinkedinEmail(linkedinEmail);
  }

  @PostMapping(value = "/uploadResume", produces = "text/plain", consumes = "multipart/form-data")
  public String uploadResume(@AuthenticationPrincipal User user, @RequestParam MultipartFile file) {
    return userService.uploadResume(user, file);
  }

  @PutMapping("/update")
  public UserResponse updateUser(@RequestBody UserRequest userRequest, @AuthenticationPrincipal User user) {
    return userService.updateUser(userRequest, user);
  }

}
