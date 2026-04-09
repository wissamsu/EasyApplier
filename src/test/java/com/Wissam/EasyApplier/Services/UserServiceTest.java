package com.Wissam.EasyApplier.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.Wissam.EasyApplier.Dto.User.UserRequest;
import com.Wissam.EasyApplier.Dto.User.UserResponse;
import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;
import com.Wissam.EasyApplier.Mapper.UserMapper;
import com.Wissam.EasyApplier.Model.Linkedin;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;

@DisplayName("UserService Tests")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepo;

  @Mock
  private UserMapper userMapper;

  @Mock
  private Cloudinary cloudinary;

  @InjectMocks
  private UserService userService;

  private User testUser;
  private UserResponse testUserResponse;

  @BeforeEach
  void setUp() {
    testUser = User.builder()
        .id(1L)
        .email("test@example.com")
        .password("encodedPassword")
        .firstName("John")
        .lastName("Doe")
        .phoneNumber("1234567890")
        .verified(true)
        .role(UserRole.ROLE_USER)
        .uuid(UUID.randomUUID())
        .build();

    testUserResponse = UserResponse.builder()
        .id(1L)
        .email("test@example.com")
        .firstName("John")
        .lastName("Doe")
        .phoneNumber("1234567890")
        .verified(true)
        .role(UserRole.ROLE_USER)
        .build();
  }

  @Nested
  @DisplayName("findUserByEmail Tests")
  class FindUserByEmailTests {

    @Test
    @DisplayName("should return user response when email exists")
    void shouldReturnUserResponseWhenEmailExists() {
      when(userRepo.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
      when(userMapper.toUserResponse(testUser)).thenReturn(testUserResponse);

      UserResponse result = userService.findUserByEmail(testUser.getEmail());

      assertThat(result).isNotNull();
      assertThat(result.getEmail()).isEqualTo(testUser.getEmail());
      verify(userRepo).findByEmail(testUser.getEmail());
      verify(userMapper).toUserResponse(testUser);
    }

    @Test
    @DisplayName("should throw UserNotFoundException when email does not exist")
    void shouldThrowExceptionWhenEmailNotFound() {
      when(userRepo.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

      assertThatThrownBy(() -> userService.findUserByEmail("nonexistent@example.com"))
          .isInstanceOf(UserNotFoundException.class)
          .hasMessageContaining("not found");

      verify(userRepo).findByEmail("nonexistent@example.com");
    }
  }

  @Nested
  @DisplayName("findUserById Tests")
  class FindUserByIdTests {

    @Test
    @DisplayName("should return user response when id exists")
    void shouldReturnUserResponseWhenIdExists() {
      when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
      when(userMapper.toUserResponse(testUser)).thenReturn(testUserResponse);

      UserResponse result = userService.findUserById(1L);

      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(1L);
      verify(userRepo).findById(1L);
    }

    @Test
    @DisplayName("should throw UserNotFoundException when id does not exist")
    void shouldThrowExceptionWhenIdNotFound() {
      when(userRepo.findById(999L)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> userService.findUserById(999L))
          .isInstanceOf(UserNotFoundException.class)
          .hasMessageContaining("not found");
    }
  }

  @Nested
  @DisplayName("getAllUsers Tests")
  class GetAllUsersTests {

    @Test
    @DisplayName("should return all users")
    void shouldReturnAllUsers() {
      List<User> users = List.of(testUser);
      List<UserResponse> userResponses = List.of(testUserResponse);

      when(userRepo.findAll()).thenReturn(users);
      when(userMapper.toUserResponseList(users)).thenReturn(userResponses);

      List<UserResponse> result = userService.getAllUsers();

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getEmail()).isEqualTo(testUser.getEmail());
      verify(userRepo).findAll();
    }

    @Test
    @DisplayName("should return empty list when no users exist")
    void shouldReturnEmptyListWhenNoUsers() {
      when(userRepo.findAll()).thenReturn(List.of());
      when(userMapper.toUserResponseList(List.of())).thenReturn(List.of());

      List<UserResponse> result = userService.getAllUsers();

      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("getAllUsersByRole Tests")
  class GetAllUsersByRoleTests {

    @Test
    @DisplayName("should return users by role")
    void shouldReturnUsersByRole() {
      List<User> users = List.of(testUser);
      List<UserResponse> userResponses = List.of(testUserResponse);

      when(userRepo.findAllByRole(UserRole.ROLE_USER)).thenReturn(users);
      when(userMapper.toUserResponseList(users)).thenReturn(userResponses);

      List<UserResponse> result = userService.getAllUsersByRole(UserRole.ROLE_USER);

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getRole()).isEqualTo(UserRole.ROLE_USER);
    }
  }

  @Nested
  @DisplayName("findUserByLinkedinId Tests")
  class FindUserByLinkedinIdTests {

    @Test
    @DisplayName("should return user by linkedin id")
    void shouldReturnUserByLinkedinId() {
      when(userRepo.findUserByLinkedin_Id(1L)).thenReturn(Optional.of(testUser));
      when(userMapper.toUserResponse(testUser)).thenReturn(testUserResponse);

      UserResponse result = userService.findUserByLinkedinId(1L);

      assertThat(result).isNotNull();
      verify(userRepo).findUserByLinkedin_Id(1L);
    }

    @Test
    @DisplayName("should throw exception when linkedin id not found")
    void shouldThrowExceptionWhenLinkedinIdNotFound() {
      when(userRepo.findUserByLinkedin_Id(999L)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> userService.findUserByLinkedinId(999L))
          .isInstanceOf(UserNotFoundException.class);
    }
  }

  @Nested
  @DisplayName("findUserByLinkedinEmail Tests")
  class FindUserByLinkedinEmailTests {

    @Test
    @DisplayName("should return user by linkedin email")
    void shouldReturnUserByLinkedinEmail() {
      when(userRepo.findUserByLinkedin_Email("linkedin@example.com")).thenReturn(Optional.of(testUser));
      when(userMapper.toUserResponse(testUser)).thenReturn(testUserResponse);

      UserResponse result = userService.findUserByLinkedinEmail("linkedin@example.com");

      assertThat(result).isNotNull();
      verify(userRepo).findUserByLinkedin_Email("linkedin@example.com");
    }

    @Test
    @DisplayName("should throw exception when linkedin email not found")
    void shouldThrowExceptionWhenLinkedinEmailNotFound() {
      when(userRepo.findUserByLinkedin_Email("nonexistent@example.com")).thenReturn(Optional.empty());

      assertThatThrownBy(() -> userService.findUserByLinkedinEmail("nonexistent@example.com"))
          .isInstanceOf(UserNotFoundException.class);
    }
  }

  @Nested
  @DisplayName("uploadResume Tests")
  class UploadResumeTests {

    @Test
    @DisplayName("should upload resume successfully")
    void shouldUploadResumeSuccessfully() throws Exception {
      MockMultipartFile file = new MockMultipartFile(
          "file", "resume.pdf", "application/pdf", "test content".getBytes());

      Linkedin linkedin = Linkedin.builder().id(1L).build();
      testUser.setLinkedin(linkedin);

      Uploader uploader = mock(Uploader.class);
      when(cloudinary.uploader()).thenReturn(uploader);
      when(uploader.upload(any(), any()))
          .thenReturn(java.util.Map.of("secure_url", "https://cloudinary.com/resume.pdf"));
      when(userRepo.save(any(User.class))).thenReturn(testUser);

      String result = userService.uploadResume(testUser, file);

      assertThat(result).isEqualTo("https://cloudinary.com/resume.pdf");
      verify(cloudinary).uploader();
      verify(userRepo).save(testUser);
    }

    @Test
    @DisplayName("should throw RuntimeException when upload fails")
    void shouldThrowRuntimeExceptionWhenUploadFails() throws Exception {
      MockMultipartFile file = new MockMultipartFile(
          "file", "resume.pdf", "application/pdf", "test content".getBytes());

      Uploader uploader = mock(Uploader.class);
      when(cloudinary.uploader()).thenReturn(uploader);
      when(uploader.upload(any(), any())).thenThrow(new RuntimeException("Upload failed"));

      assertThatThrownBy(() -> userService.uploadResume(testUser, file))
          .isInstanceOf(RuntimeException.class)
          .hasMessageContaining("Failed to upload resume");
    }
  }

  @Nested
  @DisplayName("updateUser Tests")
  class UpdateUserTests {

    @Test
    @DisplayName("should update user successfully")
    void shouldUpdateUserSuccessfully() {
      UserRequest request = new UserRequest();
      request.setFirstName("Jane");
      request.setLastName("Smith");

      when(userRepo.save(any(User.class))).thenReturn(testUser);
      when(userMapper.toUserResponse(any(User.class))).thenReturn(testUserResponse);

      UserResponse result = userService.updateUser(request, testUser);

      assertThat(result).isNotNull();
      verify(userMapper).updateUserFromRequest(testUser, request);
      verify(userRepo).save(testUser);
    }
  }
}
