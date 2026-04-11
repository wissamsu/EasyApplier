package com.Wissam.EasyApplier.Services;

import java.util.List;
import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.Wissam.EasyApplier.Dto.User.UserRequest;
import com.Wissam.EasyApplier.Dto.User.UserResponse;
import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;
import com.Wissam.EasyApplier.Mapper.UserMapper;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.Wissam.EasyApplier.Services.IServices.IUserService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

  private static final long MAX_RESUME_FILE_SIZE = 5 * 1024 * 1024;
  private static final Set<String> ALLOWED_RESUME_CONTENT_TYPES = Set.of(
      MediaType.APPLICATION_PDF_VALUE,
      "application/msword",
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

  private final UserRepository userRepo;
  private final UserMapper userMapper;
  private final Cloudinary cloudinary;

  @Override
  @Transactional(readOnly = true)
  public UserResponse findUserByEmail(String email) {
    return userMapper.toUserResponse(userRepo.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found")));
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse findUserById(Long id) {
    return userMapper.toUserResponse(
        userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found")));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponse> getAllUsers() {
    return userMapper.toUserResponseList(userRepo.findAll());
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponse> getAllUsersByRole(UserRole role) {
    return userMapper.toUserResponseList(userRepo.findAllByRole(role));
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse findUserByLinkedinId(Long linkedinId) {
    return userMapper.toUserResponse(
        userRepo.findUserByLinkedin_Id(linkedinId)
            .orElseThrow(() -> new UserNotFoundException("User with linkedinId " + linkedinId + " not found")));
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse findUserByLinkedinEmail(String linkedinEmail) {
    return userMapper.toUserResponse(
        userRepo.findUserByLinkedin_Email(linkedinEmail)
            .orElseThrow(() -> new UserNotFoundException("User with linkedinEmail " + linkedinEmail + " not found")));
  }

  @Override
  @Transactional
  public String uploadResume(User user, MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("Resume file must not be empty");
    }
    if (file.getSize() > MAX_RESUME_FILE_SIZE) {
      throw new IllegalArgumentException("Resume file must be 5 MB or smaller");
    }
    if (file.getContentType() == null || !ALLOWED_RESUME_CONTENT_TYPES.contains(file.getContentType())) {
      throw new IllegalArgumentException("Only PDF, DOC, and DOCX resumes are allowed");
    }

    try {
      var uploadResult = cloudinary.uploader().upload(
          file.getBytes(),
          ObjectUtils.asMap(
              "resource_type", "raw",
              "folder", "resumes"));

      String resumeUrl = uploadResult.get("secure_url").toString();
      user.setResumeLink(resumeUrl);
      userRepo.save(user);

      return resumeUrl;

    } catch (Exception e) {
      throw new RuntimeException("Failed to upload resume to Cloudinary", e);
    }
  }

  @Override
  @Transactional
  public UserResponse updateUser(UserRequest userRequest, User user) {
    userMapper.updateUserFromRequest(user, userRequest);
    return userMapper.toUserResponse(userRepo.save(user));
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse getCurrentUser(User user) {
    return userMapper.toUserResponse(userRepo.findById(user.getId())
        .orElseThrow(() -> new UserNotFoundException("User with id " + user.getId() + " not found")));
  }

}
