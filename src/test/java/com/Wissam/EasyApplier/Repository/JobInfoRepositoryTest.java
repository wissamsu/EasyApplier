package com.Wissam.EasyApplier.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import com.Wissam.EasyApplier.Model.JobInfo;

@DataMongoTest
@ActiveProfiles("test")
@DisplayName("JobInfoRepository Tests")
class JobInfoRepositoryTest {

  @Autowired
  private JobInfoRepository jobInfoRepository;

  private JobInfo testJobInfo;

  @BeforeEach
  void setUp() {
    jobInfoRepository.deleteAll();

    testJobInfo = JobInfo.builder()
        .id(new ObjectId())
        .jobName("Software Engineer")
        .jobLocation("New York")
        .jobCompanyName("Tech Corp")
        .jobId("JOB123")
        .jobUrl("https://example.com/job/123")
        .jobCompanyImageLink("https://example.com/logo.png")
        .build();
  }

  @Nested
  @DisplayName("findAllByJobLocation Tests")
  class FindAllByJobLocationTests {

    @Test
    @DisplayName("should find jobs by location")
    void shouldFindJobsByLocation() {
      jobInfoRepository.save(testJobInfo);

      List<JobInfo> result = jobInfoRepository.findAllByJobLocation("New York");

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getJobLocation()).isEqualTo("New York");
    }

    @Test
    @DisplayName("should return empty list when location not found")
    void shouldReturnEmptyListWhenLocationNotFound() {
      List<JobInfo> result = jobInfoRepository.findAllByJobLocation("Unknown City");

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should be case sensitive for location")
    void shouldBeCaseSensitiveForLocation() {
      jobInfoRepository.save(testJobInfo);

      List<JobInfo> result = jobInfoRepository.findAllByJobLocation("new york");

      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("findAllByJobCompanyName Tests")
  class FindAllByJobCompanyNameTests {

    @Test
    @DisplayName("should find jobs by company name")
    void shouldFindJobsByCompanyName() {
      jobInfoRepository.save(testJobInfo);

      List<JobInfo> result = jobInfoRepository.findAllByJobCompanyName("Tech Corp");

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getJobCompanyName()).isEqualTo("Tech Corp");
    }

    @Test
    @DisplayName("should return empty list when company not found")
    void shouldReturnEmptyListWhenCompanyNotFound() {
      List<JobInfo> result = jobInfoRepository.findAllByJobCompanyName("Unknown Corp");

      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("findAllByJobName Tests")
  class FindAllByJobNameTests {

    @Test
    @DisplayName("should find jobs by job name")
    void shouldFindJobsByJobName() {
      jobInfoRepository.save(testJobInfo);

      List<JobInfo> result = jobInfoRepository.findAllByJobName("Software Engineer");

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getJobName()).isEqualTo("Software Engineer");
    }

    @Test
    @DisplayName("should return empty list when job name not found")
    void shouldReturnEmptyListWhenJobNameNotFound() {
      List<JobInfo> result = jobInfoRepository.findAllByJobName("Unknown Position");

      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("existsByJobId Tests")
  class ExistsByJobIdTests {

    @Test
    @DisplayName("should return true when job exists")
    void shouldReturnTrueWhenJobExists() {
      jobInfoRepository.save(testJobInfo);

      boolean result = jobInfoRepository.existsByJobId("JOB123");

      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should return false when job does not exist")
    void shouldReturnFalseWhenJobDoesNotExist() {
      boolean result = jobInfoRepository.existsByJobId("UNKNOWN");

      assertThat(result).isFalse();
    }
  }

  @Nested
  @DisplayName("findByJobId Tests")
  class FindByJobIdTests {

    @Test
    @DisplayName("should find job by job id")
    void shouldFindJobByJobId() {
      jobInfoRepository.save(testJobInfo);

      boolean result = jobInfoRepository.existsByJobId("JOB123");

      assertThat(result).isTrue();
    }

  }
}
