package com.Wissam.EasyApplier.Controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final IUserService userService;

  @GetMapping("/email/{email}")
  public UserResponse findUserByEmail(String email) {
    return userService.findUserByEmail(email);
  }

  @GetMapping("/{id}")
  public UserResponse findUserById(Long id) {
    return userService.findUserById(id);
  }

  @GetMapping("/all")
  public List<UserResponse> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/role/{role}")
  public List<UserResponse> getAllUsersByRole(UserRole role) {
    return userService.getAllUsersByRole(role);
  }

  @GetMapping("/linkedinId/{linkedinId}")
  public UserResponse findUserByLinkedinId(Long linkedinId) {
    return userService.findUserByLinkedinId(linkedinId);
  }

  @GetMapping("/linkedinEmail/{linkedinEmail}")
  public UserResponse findUserByLinkedinEmail(String linkedinEmail) {
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
