package com.Wissam.EasyApplier.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
import com.Wissam.EasyApplier.Config.Security.OAuth2SuccessHandler;
import com.Wissam.EasyApplier.Config.Security.FilterChains.JwtFilterChain;
import com.Wissam.EasyApplier.Config.Security.SecurityUtils.JwtUtils;
import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinResponse;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.LinkedinNotFoundException;
import com.Wissam.EasyApplier.Services.IServices.ILinkedinService;
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
  @DisplayName("getLinkedinById Tests")
  class GetLinkedinByIdTests {

    @Test
    @WithMockUser
    @DisplayName("should return linkedin by id")
    void shouldReturnLinkedinById() throws Exception {
      when(linkedinService.getLinkedinById(1L)).thenReturn(testLinkedinResponse);

      mockMvc.perform(get("/linkedin/1"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.email").value("linkedin@example.com"));
    }

    @Test
    @WithMockUser
    @DisplayName("should return 404 when linkedin not found")
    void shouldReturn404WhenLinkedinNotFound() throws Exception {
      when(linkedinService.getLinkedinById(999L))
          .thenThrow(new LinkedinNotFoundException("Linkedin not found"));

      mockMvc.perform(get("/linkedin/999"))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("getLinkedinByEmail Tests")
  class GetLinkedinByEmailTests {

    @Test
    @WithMockUser
    @DisplayName("should return linkedin by email")
    void shouldReturnLinkedinByEmail() throws Exception {
      when(linkedinService.getLinkedinByEmail("linkedin@example.com")).thenReturn(testLinkedinResponse);

      mockMvc.perform(get("/linkedin/email/linkedin@example.com")
          .param("email", "linkedin@example.com"))
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

      mockMvc.perform(put("/linkedin/cookie/li_at=xxxxx"))
          .andExpect(status().isOk());
    }
  }
}
