package com.Wissam.EasyApplier.Mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Wissam.EasyApplier.Dto.JobInfo.JobInfoRequest;
import com.Wissam.EasyApplier.Dto.JobInfo.JobInfoResponse;
import com.Wissam.EasyApplier.Model.JobInfo;

@SpringBootTest
@DisplayName("JobInfoMapper Tests")
class JobInfoMapperTest {

  @Autowired
  private JobInfoMapper jobInfoMapper;

  private JobInfo testJobInfo;
  private JobInfoResponse testJobInfoResponse;
  private JobInfoRequest testJobInfoRequest;

  @BeforeEach
  void setUp() {
    testJobInfo = JobInfo.builder()
        .id(new ObjectId())
        .jobName("Software Engineer")
        .jobLocation("New York")
        .jobCompanyName("Tech Corp")
        .jobId("JOB123")
        .jobUrl("https://example.com/job/123")
        .jobCompanyImageLink("https://example.com/logo.png")
        .appliedUsers(new HashSet<>())
        .build();

    testJobInfoResponse = JobInfoResponse.builder()
        .id(testJobInfo.getId())
        .jobName("Software Engineer")
        .jobLocation("New York")
        .jobCompanyName("Tech Corp")
        .jobId("JOB123")
        .jobUrl("https://example.com/job/123")
        .jobCompanyImageLink("https://example.com/logo.png")
        .appliedUsers(new HashSet<>())
        .build();

    testJobInfoRequest = JobInfoRequest.builder()
        .jobName("Data Scientist")
        .jobLocation("San Francisco")
        .jobCompanyName("AI Corp")
        .jobId("DS456")
        .jobUrl("https://example.com/job/456")
        .jobCompanyImageLink("https://example.com/ai-logo.png")
        .build();
  }

  @Nested
  @DisplayName("toJobInfoResponse Tests")
  class ToJobInfoResponseTests {

    @Test
    @DisplayName("should convert JobInfo to JobInfoResponse")
    void shouldConvertJobInfoToJobInfoResponse() {
      JobInfoResponse result = jobInfoMapper.toJobInfoResponse(testJobInfo);

      assertThat(result).isNotNull();
      assertThat(result.getJobName()).isEqualTo(testJobInfo.getJobName());
      assertThat(result.getJobLocation()).isEqualTo(testJobInfo.getJobLocation());
      assertThat(result.getJobCompanyName()).isEqualTo(testJobInfo.getJobCompanyName());
      assertThat(result.getJobId()).isEqualTo(testJobInfo.getJobId());
    }

    @Test
    @DisplayName("should return null when JobInfo is null")
    void shouldReturnNullWhenJobInfoIsNull() {
      JobInfoResponse result = jobInfoMapper.toJobInfoResponse(null);

      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("toJobInfo Tests")
  class ToJobInfoTests {

    @Test
    @DisplayName("should convert JobInfoResponse to JobInfo")
    void shouldConvertJobInfoResponseToJobInfo() {
      JobInfo result = jobInfoMapper.toJobInfo(testJobInfoResponse);

      assertThat(result).isNotNull();
      assertThat(result.getJobName()).isEqualTo(testJobInfoResponse.getJobName());
      assertThat(result.getJobLocation()).isEqualTo(testJobInfoResponse.getJobLocation());
    }

    @Test
    @DisplayName("should convert JobInfoRequest to JobInfo")
    void shouldConvertJobInfoRequestToJobInfo() {
      JobInfo result = jobInfoMapper.toJobInfo(testJobInfoRequest);

      assertThat(result).isNotNull();
      assertThat(result.getJobName()).isEqualTo(testJobInfoRequest.getJobName());
      assertThat(result.getJobLocation()).isEqualTo(testJobInfoRequest.getJobLocation());
      assertThat(result.getJobCompanyName()).isEqualTo(testJobInfoRequest.getJobCompanyName());
    }
  }

  @Nested
  @DisplayName("toJobInfoRequest Tests")
  class ToJobInfoRequestTests {

    @Test
    @DisplayName("should convert JobInfo to JobInfoRequest")
    void shouldConvertJobInfoToJobInfoRequest() {
      JobInfoRequest result = jobInfoMapper.toJobInfoRequest(testJobInfo);

      assertThat(result).isNotNull();
      assertThat(result.getJobName()).isEqualTo(testJobInfo.getJobName());
      assertThat(result.getJobLocation()).isEqualTo(testJobInfo.getJobLocation());
    }
  }

  @Nested
  @DisplayName("toJobInfoResponses Tests")
  class ToJobInfoResponsesTests {

    @Test
    @DisplayName("should convert list of JobInfo to list of JobInfoResponse")
    void shouldConvertListOfJobInfoToListOfJobInfoResponse() {
      List<JobInfo> jobInfoList = List.of(testJobInfo);

      List<JobInfoResponse> result = jobInfoMapper.toJobInfoResponses(jobInfoList);

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getJobName()).isEqualTo(testJobInfo.getJobName());
    }

    @Test
    @DisplayName("should return empty list when input is empty")
    void shouldReturnEmptyListWhenInputIsEmpty() {
      List<JobInfoResponse> result = jobInfoMapper.toJobInfoResponses(List.of());

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should return null when input is null")
    void shouldReturnNullWhenInputIsNull() {
      List<JobInfoResponse> result = jobInfoMapper.toJobInfoResponses(null);

      assertThat(result).isNull();
    }
  }
}
