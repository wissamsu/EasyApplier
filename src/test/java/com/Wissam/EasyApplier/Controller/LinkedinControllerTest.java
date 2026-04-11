package com.Wissam.EasyApplier.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.Wissam.EasyApplier.Config.Security.CustomUserDetailsService;
import com.Wissam.EasyApplier.Config.Security.FilterChains.JwtFilterChain;
import com.Wissam.EasyApplier.Config.Security.OAuth2SuccessHandler;
import com.Wissam.EasyApplier.Config.Security.SecurityUtils.JwtUtils;
import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinResponse;
import com.Wissam.EasyApplier.Services.IServices.ILinkedinService;
import com.Wissam.EasyApplier.Utils.LinkedinUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(LinkedinController.class)
@DisplayName("LinkedinController Tests")
class LinkedinControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ILinkedinService linkedinService;

  @MockitoBean
  private CustomUserDetailsService userDetailsService;

  @MockitoBean
  private JwtUtils jwtUtils;

  @MockitoBean
  private JwtFilterChain jwtFilterChain;

  @MockitoBean
  private OAuth2SuccessHandler oauth2SuccessHandler;

  @MockitoBean
  private LinkedinUtils linkedinUtils;

  @Autowired
  private ObjectMapper objectMapper;

  private LinkedinResponse testLinkedinResponse;

  @BeforeEach
  void setUp() {
    testLinkedinResponse = LinkedinResponse.builder()
        .id(1L)
        .email("linkedin@example.com")
        .build();
  }

  @Nested
  @DisplayName("getLinkedin Tests")
  class GetLinkedinTests {

    @Test
    @WithMockUser
    @DisplayName("should return current users linkedin profile")
    void shouldReturnCurrentUsersLinkedinProfile() throws Exception {
      when(linkedinService.getLinkedin(any())).thenReturn(testLinkedinResponse);

      mockMvc.perform(get("/linkedin/me"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.email").value("linkedin@example.com"));
    }
  }

  @Nested
  @DisplayName("admin lookup Tests")
  class AdminLookupTests {

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("should return linkedin by id for admin")
    void shouldReturnLinkedinByIdForAdmin() throws Exception {
      when(linkedinService.getLinkedinById(1L)).thenReturn(testLinkedinResponse);

      mockMvc.perform(get("/linkedin/1"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.email").value("linkedin@example.com"));
    }

    @Test
    @WithMockUser
    @DisplayName("should forbid linkedin lookup by id for non admin")
    void shouldForbidLinkedinLookupByIdForNonAdmin() throws Exception {
      mockMvc.perform(get("/linkedin/999"))
          .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("should return linkedin by email for admin")
    void shouldReturnLinkedinByEmailForAdmin() throws Exception {
      when(linkedinService.getLinkedinByEmail("linkedin@example.com")).thenReturn(testLinkedinResponse);

      mockMvc.perform(get("/linkedin/email/linkedin@example.com"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.email").value("linkedin@example.com"));
    }
  }

  @Nested
  @DisplayName("addLiAtCookie Tests")
  class AddLiAtCookieTests {

    @Test
    @WithMockUser
    @DisplayName("should add li_at cookie")
    void shouldAddLiAtCookie() throws Exception {
      when(linkedinService.addLi_AtCookie(any(), any())).thenReturn(testLinkedinResponse);

      mockMvc.perform(put("/linkedin/cookie")
          .with(csrf())
          .contentType("text/plain")
          .content("li_at=xxxxx"))
          .andExpect(status().isOk());
    }
  }
}
