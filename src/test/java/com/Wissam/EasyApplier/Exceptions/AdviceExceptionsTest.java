package com.Wissam.EasyApplier.Exceptions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.Wissam.EasyApplier.Config.Security.CustomUserDetailsService;
import com.Wissam.EasyApplier.Config.Security.OAuth2SuccessHandler;
import com.Wissam.EasyApplier.Config.Security.FilterChains.JwtFilterChain;
import com.Wissam.EasyApplier.Config.Security.SecurityUtils.JwtUtils;
import com.Wissam.EasyApplier.Exceptions.ControllerExceptions.AdviceExceptions;
import com.Wissam.EasyApplier.Messaging.KafkaEventPublisher;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.Wissam.EasyApplier.Services.IServices.IAuthService;

@WebMvcTest(controllers = AdviceExceptions.class)
@DisplayName("AdviceExceptions Tests")
class AdviceExceptionsTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private IAuthService authService;

  @MockitoBean
  private JwtUtils jwtUtils;

  @MockitoBean
  private UserRepository userRepo;

  @MockitoBean
  private KafkaEventPublisher kafkaEventPublisher;

  @MockitoBean
  private CustomUserDetailsService userDetailsService;

  @MockitoBean
  private JwtFilterChain jwtFilterChain;

  @MockitoBean
  private OAuth2SuccessHandler oauth2SuccessHandler;

  @Nested
  @DisplayName("LinkedinNotFoundException Handler Tests")
  class LinkedinNotFoundExceptionHandlerTests {

    @Test
    @WithMockUser
    @DisplayName("should return 404 for LinkedinNotFoundException")
    void shouldReturn404ForLinkedinNotFoundException() throws Exception {
      mockMvc.perform(get("/non-existent-linkedin"))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("UserNotFoundException Handler Tests")
  class UserNotFoundExceptionHandlerTests {

    @Test
    @WithMockUser
    @DisplayName("should return 404 for UserNotFoundException")
    void shouldReturn404ForUserNotFoundException() throws Exception {
      mockMvc.perform(get("/non-existent-user"))
          .andExpect(status().isNotFound());
    }
  }
}
