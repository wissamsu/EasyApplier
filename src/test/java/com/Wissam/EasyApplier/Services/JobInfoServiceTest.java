package com.Wissam.EasyApplier.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Wissam.EasyApplier.Dto.JobInfo.JobInfoResponse;
import com.Wissam.EasyApplier.Mapper.JobInfoMapper;
import com.Wissam.EasyApplier.Model.JobInfo;
import com.Wissam.EasyApplier.Repository.JobInfoRepository;

@DisplayName("JobInfoService Tests")
@ExtendWith(MockitoExtension.class)
class JobInfoServiceTest {

    @Mock
    private JobInfoRepository jobInfoRepo;

    @Mock
    private JobInfoMapper jobInfoMapper;

    @InjectMocks
    private JobInfoService jobInfoService;

    private JobInfo testJobInfo;
    private JobInfoResponse testJobInfoResponse;

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
                .build();

        testJobInfoResponse = JobInfoResponse.builder()
                .id(testJobInfo.getId())
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
        @DisplayName("should return jobs by location")
        void shouldReturnJobsByLocation() {
            List<JobInfo> jobs = List.of(testJobInfo);
            List<JobInfoResponse> responses = List.of(testJobInfoResponse);

            when(jobInfoRepo.findAllByJobLocation("New York")).thenReturn(jobs);
            when(jobInfoMapper.toJobInfoResponses(jobs)).thenReturn(responses);

            List<JobInfoResponse> result = jobInfoService.findAllByJobLocation("New York");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getJobLocation()).isEqualTo("New York");
            verify(jobInfoRepo).findAllByJobLocation("New York");
        }

        @Test
        @DisplayName("should return empty list when no jobs found at location")
        void shouldReturnEmptyListWhenNoJobsFoundAtLocation() {
            when(jobInfoRepo.findAllByJobLocation("Unknown City")).thenReturn(List.of());
            when(jobInfoMapper.toJobInfoResponses(List.of())).thenReturn(List.of());

            List<JobInfoResponse> result = jobInfoService.findAllByJobLocation("Unknown City");

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllByJobCompanyName Tests")
    class FindAllByJobCompanyNameTests {

        @Test
        @DisplayName("should return jobs by company name")
        void shouldReturnJobsByCompanyName() {
            List<JobInfo> jobs = List.of(testJobInfo);
            List<JobInfoResponse> responses = List.of(testJobInfoResponse);

            when(jobInfoRepo.findAllByJobCompanyName("Tech Corp")).thenReturn(jobs);
            when(jobInfoMapper.toJobInfoResponses(jobs)).thenReturn(responses);

            List<JobInfoResponse> result = jobInfoService.findAllByJobCompanyName("Tech Corp");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getJobCompanyName()).isEqualTo("Tech Corp");
        }

        @Test
        @DisplayName("should return empty list when company has no jobs")
        void shouldReturnEmptyListWhenCompanyHasNoJobs() {
            when(jobInfoRepo.findAllByJobCompanyName("Unknown Corp")).thenReturn(List.of());
            when(jobInfoMapper.toJobInfoResponses(List.of())).thenReturn(List.of());

            List<JobInfoResponse> result = jobInfoService.findAllByJobCompanyName("Unknown Corp");

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllByJobName Tests")
    class FindAllByJobNameTests {

        @Test
        @DisplayName("should return jobs by job name")
        void shouldReturnJobsByJobName() {
            List<JobInfo> jobs = List.of(testJobInfo);
            List<JobInfoResponse> responses = List.of(testJobInfoResponse);

            when(jobInfoRepo.findAllByJobName("Software Engineer")).thenReturn(jobs);
            when(jobInfoMapper.toJobInfoResponses(jobs)).thenReturn(responses);

            List<JobInfoResponse> result = jobInfoService.findAllByJobName("Software Engineer");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getJobName()).isEqualTo("Software Engineer");
        }
    }

    @Nested
    @DisplayName("findAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("should return all jobs")
        void shouldReturnAllJobs() {
            List<JobInfo> jobs = List.of(testJobInfo);
            List<JobInfoResponse> responses = List.of(testJobInfoResponse);

            when(jobInfoRepo.findAll()).thenReturn(jobs);
            when(jobInfoMapper.toJobInfoResponses(jobs)).thenReturn(responses);

            List<JobInfoResponse> result = jobInfoService.findAll();

            assertThat(result).hasSize(1);
            verify(jobInfoRepo).findAll();
        }

        @Test
        @DisplayName("should return empty list when no jobs exist")
        void shouldReturnEmptyListWhenNoJobsExist() {
            when(jobInfoRepo.findAll()).thenReturn(List.of());
            when(jobInfoMapper.toJobInfoResponses(List.of())).thenReturn(List.of());

            List<JobInfoResponse> result = jobInfoService.findAll();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("save Tests")
    class SaveTests {

        @Test
        @DisplayName("should save job info")
        void shouldSaveJobInfo() {
            when(jobInfoRepo.save(testJobInfo)).thenReturn(testJobInfo);

            jobInfoService.save(testJobInfo);

            verify(jobInfoRepo).save(testJobInfo);
        }
    }

    @Nested
    @DisplayName("existsByJobId Tests")
    class ExistsByJobIdTests {

        @Test
        @DisplayName("should return true when job exists")
        void shouldReturnTrueWhenJobExists() {
            when(jobInfoRepo.existsByJobId("JOB123")).thenReturn(true);

            boolean result = jobInfoService.existsByJobId("JOB123");

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("should return false when job does not exist")
        void shouldReturnFalseWhenJobDoesNotExist() {
            when(jobInfoRepo.existsByJobId("UNKNOWN")).thenReturn(false);

            boolean result = jobInfoService.existsByJobId("UNKNOWN");

            assertThat(result).isFalse();
        }
    }
}