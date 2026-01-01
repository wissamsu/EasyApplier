package com.Wissam.EasyApplier.Services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Wissam.EasyApplier.Dto.User.UserResponse;
import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;
import com.Wissam.EasyApplier.Mapper.UserMapper;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.Wissam.EasyApplier.Services.IServices.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements IUserService {

  private final UserRepository userRepo;
  private final UserMapper userMapper;

  @Override
  public UserResponse findUserByEmail(String email) {
    return userMapper.toUserResponse(userRepo.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found")));
  }

  @Override
  public UserResponse findUserById(Long id) {
    return userMapper.toUserResponse(
        userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found")));
  }

  @Override
  public List<UserResponse> getAllUsers() {
    return userMapper.toUserResponseList(userRepo.findAll());
  }

  @Override
  public List<UserResponse> getAllUsersByRole(UserRole role) {
    return userMapper.toUserResponseList(userRepo.findAllByRole(role));
  }

  @Override
  public UserResponse findUserByLinkedinId(Long linkedinId) {
    return userMapper.toUserResponse(
        userRepo.findUserByLinkedin_Id(linkedinId)
            .orElseThrow(() -> new UserNotFoundException("User with linkedinId " + linkedinId + " not found")));
  }

  @Override
  public UserResponse findUserByLinkedinEmail(String linkedinEmail) {
    return userMapper.toUserResponse(
        userRepo.findUserByLinkedin_Email(linkedinEmail)
            .orElseThrow(() -> new UserNotFoundException("User with linkedinEmail " + linkedinEmail + " not found")));
  }

}
