package com.Wissam.EasyApplier.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.Wissam.EasyApplier.Config.Security.CustomUserDetailsService;
import com.Wissam.EasyApplier.Config.Security.OAuth2SuccessHandler;
import com.Wissam.EasyApplier.Config.Security.FilterChains.JwtFilterChain;
import com.Wissam.EasyApplier.Config.Security.SecurityUtils.JwtUtils;
import com.Wissam.EasyApplier.Dto.User.UserRequest;
import com.Wissam.EasyApplier.Dto.User.UserResponse;
import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;
import com.Wissam.EasyApplier.Services.IServices.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@DisplayName("UserController Tests")
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private IUserService userService;

  @MockitoBean
  private CustomUserDetailsService userDetailsService;

  @MockitoBean
  private JwtUtils jwtUtils;

  @MockitoBean
  private JwtFilterChain jwtFilterChain;

  @MockitoBean
  private OAuth2SuccessHandler oauth2SuccessHandler;

  @Autowired
  private ObjectMapper objectMapper;

  private UserResponse testUserResponse;

  @BeforeEach
  void setUp() {
    testUserResponse = UserResponse.builder()
        .id(1L)
        .email("test@example.com")
        .firstName("John")
        .lastName("Doe")
        .phoneNumber("1234567890")
        .verified(true)
        .role(UserRole.ROLE_USER)
        .uuid(UUID.randomUUID())
        .build();
  }

  @Nested
  @DisplayName("findUserByEmail Tests")
  class FindUserByEmailTests {

    @Test
    @WithMockUser
    @DisplayName("should return user by email")
    void shouldReturnUserByEmail() throws Exception {
      when(userService.findUserByEmail("test@example.com")).thenReturn(testUserResponse);

      mockMvc.perform(get("/user/email/test@example.com")
          .param("email", "test@example.com"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.email").value("test@example.com"))
          .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @WithMockUser
    @DisplayName("should throw exception when user not found by email")
    void shouldThrowExceptionWhenUserNotFoundByEmail() throws Exception {
      when(userService.findUserByEmail("nonexistent@example.com"))
          .thenThrow(new UserNotFoundException("User not found"));

      mockMvc.perform(get("/user/email/nonexistent@example.com")
          .param("email", "nonexistent@example.com"))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("findUserById Tests")
  class FindUserByIdTests {

    @Test
    @WithMockUser
    @DisplayName("should return user by id")
    void shouldReturnUserById() throws Exception {
      when(userService.findUserById(1L)).thenReturn(testUserResponse);

      mockMvc.perform(get("/user/1"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser
    @DisplayName("should throw exception when user not found by id")
    void shouldThrowExceptionWhenUserNotFoundById() throws Exception {
      when(userService.findUserById(999L))
          .thenThrow(new UserNotFoundException("User not found"));

      mockMvc.perform(get("/user/999"))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("getAllUsers Tests")
  class GetAllUsersTests {

    @Test
    @WithMockUser
    @DisplayName("should return all users")
    void shouldReturnAllUsers() throws Exception {
      when(userService.getAllUsers()).thenReturn(List.of(testUserResponse));

      mockMvc.perform(get("/user/all"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    @WithMockUser
    @DisplayName("should return empty list when no users")
    void shouldReturnEmptyListWhenNoUsers() throws Exception {
      when(userService.getAllUsers()).thenReturn(List.of());

      mockMvc.perform(get("/user/all"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").isEmpty());
    }
  }

  @Nested
  @DisplayName("getAllUsersByRole Tests")
  class GetAllUsersByRoleTests {

    @Test
    @WithMockUser
    @DisplayName("should return users by role")
    void shouldReturnUsersByRole() throws Exception {
      when(userService.getAllUsersByRole(UserRole.ROLE_USER)).thenReturn(List.of(testUserResponse));

      mockMvc.perform(get("/user/role/ROLE_USER"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].role").value("ROLE_USER"));
    }
  }

  @Nested
  @DisplayName("findUserByLinkedinId Tests")
  class FindUserByLinkedinIdTests {

    @Test
    @WithMockUser
    @DisplayName("should return user by linkedin id")
    void shouldReturnUserByLinkedinId() throws Exception {
      when(userService.findUserByLinkedinId(1L)).thenReturn(testUserResponse);

      mockMvc.perform(get("/user/linkedinId/1"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1));
    }
  }

  @Nested
  @DisplayName("updateUser Tests")
  class UpdateUserTests {

    @Test
    @WithMockUser
    @DisplayName("should update user successfully")
    void shouldUpdateUserSuccessfully() throws Exception {
      UserRequest request = new UserRequest();
      request.setFirstName("Jane");
      request.setLastName("Smith");

      UserResponse updatedResponse = UserResponse.builder()
          .id(1L)
          .email("test@example.com")
          .firstName("Jane")
          .lastName("Smith")
          .role(UserRole.ROLE_USER)
          .build();

      when(userService.updateUser(any(UserRequest.class), any())).thenReturn(updatedResponse);

      mockMvc.perform(put("/user/update")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.firstName").value("Jane"))
          .andExpect(jsonPath("$.lastName").value("Smith"));
    }
  }
}
