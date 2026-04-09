package com.Wissam.EasyApplier.Controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.Wissam.EasyApplier.Config.Security.CustomUserDetailsService;
import com.Wissam.EasyApplier.Config.Security.OAuth2SuccessHandler;
import com.Wissam.EasyApplier.Config.Security.FilterChains.JwtFilterChain;
import com.Wissam.EasyApplier.Config.Security.SecurityUtils.JwtUtils;
import com.Wissam.EasyApplier.Dto.JobInfo.JobInfoResponse;
import com.Wissam.EasyApplier.Services.JobInfoService;

@WebMvcTest(JobInfoController.class)
@DisplayName("JobInfoController Tests")
class JobInfoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private JobInfoService jobInfoService;

  @MockitoBean
  private CustomUserDetailsService userDetailsService;

  @MockitoBean
  private JwtUtils jwtUtils;

  @MockitoBean
  private JwtFilterChain jwtFilterChain;

  @MockitoBean
  private OAuth2SuccessHandler oauth2SuccessHandler;

  private JobInfoResponse testJobInfoResponse;

  @BeforeEach
  void setUp() {
    testJobInfoResponse = JobInfoResponse.builder()
        .id(new ObjectId())
        .jobName("Software Engineer")
        .jobLocation("New York")
        .jobCompanyName("Tech Corp")
        .jobId("JOB123")
        .jobUrl("https://example.com/job/123")
        .jobCompanyImageLink("https://example.com/logo.png")
        .appliedUsers(new HashSet<>())
        .build();
  }

  @Nested
  @DisplayName("findAllByJobLocation Tests")
  class FindAllByJobLocationTests {

    @Test
    @DisplayName("should return jobs by location")
    void shouldReturnJobsByLocation() throws Exception {
      when(jobInfoService.findAllByJobLocation("New York")).thenReturn(List.of(testJobInfoResponse));

      mockMvc.perform(get("/jobInfo/all/New York"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].jobLocation").value("New York"))
          .andExpect(jsonPath("$[0].jobName").value("Software Engineer"));
    }

    @Test
    @DisplayName("should return empty list when no jobs at location")
    void shouldReturnEmptyListWhenNoJobsAtLocation() throws Exception {
      when(jobInfoService.findAllByJobLocation("Unknown City")).thenReturn(List.of());

      mockMvc.perform(get("/jobInfo/all/Unknown City"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").isEmpty());
    }
  }

  @Nested
  @DisplayName("findAllByJobCompanyName Tests")
  class FindAllByJobCompanyNameTests {

    @Test
    @DisplayName("should return jobs by company name")
    void shouldReturnJobsByCompanyName() throws Exception {
      when(jobInfoService.findAllByJobCompanyName("Tech Corp")).thenReturn(List.of(testJobInfoResponse));

      mockMvc.perform(get("/jobInfo/jobCompanyName/Tech Corp"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].jobCompanyName").value("Tech Corp"));
    }
  }

  @Nested
  @DisplayName("findAllByJobName Tests")
  class FindAllByJobNameTests {

    @Test
    @DisplayName("should return jobs by job name")
    void shouldReturnJobsByJobName() throws Exception {
      when(jobInfoService.findAllByJobName("Software Engineer")).thenReturn(List.of(testJobInfoResponse));

      mockMvc.perform(get("/jobInfo/jobName/Software Engineer"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].jobName").value("Software Engineer"));
    }
  }

  @Nested
  @DisplayName("findAll Tests")
  class FindAllTests {

    @Test
    @DisplayName("should return all jobs")
    void shouldReturnAllJobs() throws Exception {
      when(jobInfoService.findAll()).thenReturn(List.of(testJobInfoResponse));

      mockMvc.perform(get("/jobInfo/all"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].jobName").value("Software Engineer"));
    }

    @Test
    @DisplayName("should return empty list when no jobs exist")
    void shouldReturnEmptyListWhenNoJobsExist() throws Exception {
      when(jobInfoService.findAll()).thenReturn(List.of());

      mockMvc.perform(get("/jobInfo/all"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").isEmpty());
    }
  }
}
