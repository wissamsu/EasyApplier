package com.Wissam.EasyApplier.Services.IServices;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.Wissam.EasyApplier.Dto.User.UserRequest;
import com.Wissam.EasyApplier.Dto.User.UserResponse;
import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Model.User;

public interface IUserService {

  public UserResponse findUserByEmail(String email);

  public UserResponse findUserById(Long id);

  public List<UserResponse> getAllUsers();

  public List<UserResponse> getAllUsersByRole(UserRole role);

  public UserResponse findUserByLinkedinId(Long linkedinId);

  public UserResponse findUserByLinkedinEmail(String linkedinEmail);

  public String uploadResume(User user, MultipartFile file);

  public UserResponse updateUser(UserRequest userRequest, User user);

}
