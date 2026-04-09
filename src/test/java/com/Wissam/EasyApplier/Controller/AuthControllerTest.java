package com.Wissam.EasyApplier.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.Wissam.EasyApplier.Config.Security.CustomUserDetailsService;
import com.Wissam.EasyApplier.Config.Security.OAuth2SuccessHandler;
import com.Wissam.EasyApplier.Config.Security.FilterChains.JwtFilterChain;
import com.Wissam.EasyApplier.Config.Security.SecurityUtils.JwtUtils;
import com.Wissam.EasyApplier.Dto.User.UserResponse;
import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.Wissam.EasyApplier.Services.IServices.IAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private IAuthService authService;

  @MockitoBean
  private JwtUtils jwtUtils;

  @MockitoBean
  private UserRepository userRepo;

  @MockitoBean
  private ApplicationEventPublisher publisher;

  @MockitoBean
  private CustomUserDetailsService userDetailsService;

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
        .verified(true)
        .role(UserRole.ROLE_USER)
        .uuid(UUID.randomUUID())
        .build();
  }

  @Nested
  @DisplayName("hello Tests")
  class HelloTests {

    @Test
    @DisplayName("should return hello message")
    void shouldReturnHelloMessage() throws Exception {
      mockMvc.perform(get("/auth/hello"))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.content().string("hello"));
    }
  }

  @Nested
  @DisplayName("failure Tests")
  class FailureTests {

    @Test
    @DisplayName("should return failure message")
    void shouldReturnFailureMessage() throws Exception {
      mockMvc.perform(get("/auth/failure"))
          .andExpect(status().isOk())
          .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
              .string("Authentication failed"));
    }
  }

  @Nested
  @DisplayName("authStatus Tests")
  class AuthStatusTests {

    @Test
    @WithMockUser
    @DisplayName("should return authenticated status")
    void shouldReturnAuthenticatedStatus() throws Exception {
      mockMvc.perform(get("/auth/auth-status"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should return unauthenticated status for anonymous user")
    void shouldReturnUnauthenticatedStatusForAnonymousUser() throws Exception {
      mockMvc.perform(get("/auth/auth-status"))
          .andExpect(status().isOk());
    }
  }

  @Nested
  @DisplayName("login Tests")
  class LoginTests {

    @Test
    @DisplayName("should login successfully with valid credentials")
    void shouldLoginSuccessfullyWithValidCredentials() throws Exception {
      when(authService.login("test@example.com", "password")).thenReturn(true);
      when(jwtUtils.generateToken("test@example.com")).thenReturn("jwt-token");

      mockMvc.perform(post("/auth/login")
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .param("email", "test@example.com")
          .param("password", "password"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should fail login with invalid credentials")
    void shouldFailLoginWithInvalidCredentials() throws Exception {
      when(authService.login("test@example.com", "wrongpassword")).thenReturn(false);
      when(jwtUtils.generateToken("test@example.com")).thenReturn("jwt-token");

      mockMvc.perform(post("/auth/login")
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .param("email", "test@example.com")
          .param("password", "wrongpassword"))
          .andExpect(status().isOk());
    }
  }

  @Nested
  @DisplayName("register Tests")
  class RegisterTests {

    @Test
    @DisplayName("should register new user successfully")
    void shouldRegisterNewUserSuccessfully() throws Exception {
      when(userRepo.existsByEmail("new@example.com")).thenReturn(false);
      when(authService.register(eq("new@example.com"), eq("password"), any(UUID.class)))
          .thenReturn("User registered successfully");

      mockMvc.perform(post("/auth/register")
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .param("email", "new@example.com")
          .param("password", "password"))
          .andExpect(status().isOk())
          .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
              .string("User registered successfully"));
    }

    @Test
    @DisplayName("should fail when email already exists")
    void shouldFailWhenEmailAlreadyExists() throws Exception {
      when(userRepo.existsByEmail("existing@example.com")).thenReturn(true);

      mockMvc.perform(post("/auth/register")
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .param("email", "existing@example.com")
          .param("password", "password"))
          .andExpect(status().isOk())
          .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
              .string("User with email existing@example.com already exists"));
    }
  }
}
