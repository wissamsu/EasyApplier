package com.Wissam.EasyApplier.Services.IServices;

import java.util.List;

import com.Wissam.EasyApplier.Dto.User.UserResponse;
import com.Wissam.EasyApplier.Enums.UserRole;

public interface IUserService {

  public UserResponse findUserByEmail(String email);

  public UserResponse findUserById(Long id);

  public List<UserResponse> getAllUsers();

  public List<UserResponse> getAllUsersByRole(UserRole role);

  public UserResponse findUserByLinkedinId(Long linkedinId);

  public UserResponse findUserByLinkedinEmail(String linkedinEmail);

}
