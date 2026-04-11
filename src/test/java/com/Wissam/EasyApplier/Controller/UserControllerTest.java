package com.Wissam.EasyApplier.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import com.Wissam.EasyApplier.Config.Security.FilterChains.JwtFilterChain;
import com.Wissam.EasyApplier.Config.Security.OAuth2SuccessHandler;
import com.Wissam.EasyApplier.Config.Security.SecurityUtils.JwtUtils;
import com.Wissam.EasyApplier.Dto.User.UserRequest;
import com.Wissam.EasyApplier.Dto.User.UserResponse;
import com.Wissam.EasyApplier.Enums.UserRole;
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
        .build();
  }

  @Nested
  @DisplayName("getCurrentUser Tests")
  class GetCurrentUserTests {

    @Test
    @WithMockUser
    @DisplayName("should return current user")
    void shouldReturnCurrentUser() throws Exception {
      when(userService.getCurrentUser(any())).thenReturn(testUserResponse);

      mockMvc.perform(get("/user/me"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.email").value("test@example.com"));
    }
  }

  @Nested
  @DisplayName("admin lookup Tests")
  class AdminLookupTests {

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("should return user by email for admin")
    void shouldReturnUserByEmailForAdmin() throws Exception {
      when(userService.findUserByEmail("test@example.com")).thenReturn(testUserResponse);

      mockMvc.perform(get("/user/email/test@example.com"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser
    @DisplayName("should forbid user lookup by email for non admin")
    void shouldForbidUserLookupByEmailForNonAdmin() throws Exception {
      mockMvc.perform(get("/user/email/test@example.com"))
          .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("should return user by id for admin")
    void shouldReturnUserByIdForAdmin() throws Exception {
      when(userService.findUserById(1L)).thenReturn(testUserResponse);

      mockMvc.perform(get("/user/1"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("should return all users for admin")
    void shouldReturnAllUsersForAdmin() throws Exception {
      when(userService.getAllUsers()).thenReturn(List.of(testUserResponse));

      mockMvc.perform(get("/user/all"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    @WithMockUser
    @DisplayName("should forbid listing all users for non admin")
    void shouldForbidListingAllUsersForNonAdmin() throws Exception {
      mockMvc.perform(get("/user/all"))
          .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("should return users by role for admin")
    void shouldReturnUsersByRoleForAdmin() throws Exception {
      when(userService.getAllUsersByRole(UserRole.ROLE_USER)).thenReturn(List.of(testUserResponse));

      mockMvc.perform(get("/user/role/ROLE_USER"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].role").value("ROLE_USER"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("should return user by linkedin id for admin")
    void shouldReturnUserByLinkedinIdForAdmin() throws Exception {
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
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.firstName").value("Jane"))
          .andExpect(jsonPath("$.lastName").value("Smith"));
    }
  }
}
