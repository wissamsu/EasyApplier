package com.Wissam.EasyApplier.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Wissam.EasyApplier.Dto.JobAnswer.JobAnswerRequest;
import com.Wissam.EasyApplier.Dto.JobAnswer.JobAnswerResponse;
import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.JobAnswersNotFoundExceptions;
import com.Wissam.EasyApplier.Mapper.JobAnswerMapper;
import com.Wissam.EasyApplier.Model.JobAnswer;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.JobAnswersRepository;

@DisplayName("JobAnswerService Tests")
@ExtendWith(MockitoExtension.class)
class JobAnswerServiceTest {

  @Mock
  private JobAnswersRepository jobAnswerRepository;

  @Mock
  private JobAnswerMapper jobAnswerMapper;

  @InjectMocks
  private JobAnswerService jobAnswerService;

  private User testUser;
  private JobAnswer testJobAnswer;
  private JobAnswerRequest testJobAnswerRequest;
  private JobAnswerResponse testJobAnswerResponse;

  @BeforeEach
  void setUp() {
    testUser = User.builder()
        .id(1L)
        .email("test@example.com")
        .password("password")
        .role(UserRole.ROLE_USER)
        .verified(true)
        .build();

    testJobAnswer = JobAnswer.builder()
        .id(1L)
        .user(testUser)
        .address("123 Main St")
        .city("New York")
        .state("NY")
        .zipCode("10001")
        .country("USA")
        .linkedinProfileUrl("https://linkedin.com/in/test")
        .yearsOfExperience((short) 5)
        .eligibleToWorkInUS(true)
        .requireVisa(false)
        .build();

    testJobAnswerRequest = JobAnswerRequest.builder()
        .address("123 Main St")
        .city("New York")
        .state("NY")
        .zipCode("10001")
        .country("USA")
        .linkedinProfileUrl("https://linkedin.com/in/test")
        .yearsOfExperience((short) 5)
        .eligibleToWorkInUS(true)
        .requireVisa(false)
        .build();

    testJobAnswerResponse = JobAnswerResponse.builder()
        .id(1L)
        .address("123 Main St")
        .city("New York")
        .state("NY")
        .zipCode("10001")
        .country("USA")
        .linkedinProfileUrl("https://linkedin.com/in/test")
        .yearsOfExperience((short) 5)
        .eligibleToWorkInUS(true)
        .requireVisa(false)
        .build();
  }

  @Nested
  @DisplayName("getJobAnswers Tests")
  class GetJobAnswersTests {

    @Test
    @DisplayName("should return job answers for user")
    void shouldReturnJobAnswersForUser() {
      when(jobAnswerRepository.findByUser(testUser)).thenReturn(Optional.of(testJobAnswer));
      when(jobAnswerMapper.toJobAnswerResponse(testJobAnswer)).thenReturn(testJobAnswerResponse);

      JobAnswerResponse result = jobAnswerService.getJobAnswers(testUser);

      assertThat(result).isNotNull();
      assertThat(result.getAddress()).isEqualTo("123 Main St");
      assertThat(result.getCity()).isEqualTo("New York");
      verify(jobAnswerRepository).findByUser(testUser);
    }

    @Test
    @DisplayName("should throw exception when job answers not found")
    void shouldThrowExceptionWhenJobAnswersNotFound() {
      when(jobAnswerRepository.findByUser(testUser)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> jobAnswerService.getJobAnswers(testUser))
          .isInstanceOf(JobAnswersNotFoundExceptions.class)
          .hasMessageContaining("not found");
    }
  }

  @Nested
  @DisplayName("saveJobAnswers Tests")
  class SaveJobAnswersTests {

    @Test
    @DisplayName("should save job answers successfully")
    void shouldSaveJobAnswersSuccessfully() {
      when(jobAnswerMapper.toJobAnswer(testJobAnswerRequest)).thenReturn(testJobAnswer);
      when(jobAnswerRepository.save(testJobAnswer)).thenReturn(testJobAnswer);
      when(jobAnswerMapper.toJobAnswerResponse(testJobAnswer)).thenReturn(testJobAnswerResponse);

      JobAnswerResponse result = jobAnswerService.saveJobAnswers(testUser, testJobAnswerRequest);

      assertThat(result).isNotNull();
      verify(jobAnswerMapper).toJobAnswer(testJobAnswerRequest);
      verify(jobAnswerRepository).save(testJobAnswer);
    }

    @Test
    @DisplayName("should set user on job answer when saving")
    void shouldSetUserOnJobAnswerWhenSaving() {
      when(jobAnswerMapper.toJobAnswer(testJobAnswerRequest)).thenReturn(testJobAnswer);
      when(jobAnswerRepository.save(any(JobAnswer.class))).thenReturn(testJobAnswer);
      when(jobAnswerMapper.toJobAnswerResponse(any(JobAnswer.class))).thenReturn(testJobAnswerResponse);

      jobAnswerService.saveJobAnswers(testUser, testJobAnswerRequest);

      assertThat(testJobAnswer.getUser()).isEqualTo(testUser);
    }
  }

  @Nested
  @DisplayName("updateJobAnswers Tests")
  class UpdateJobAnswersTests {

    @Test
    @DisplayName("should update job answers successfully")
    void shouldUpdateJobAnswersSuccessfully() {
      when(jobAnswerRepository.findByUser(testUser)).thenReturn(Optional.of(testJobAnswer));
      when(jobAnswerRepository.save(testJobAnswer)).thenReturn(testJobAnswer);
      when(jobAnswerMapper.toJobAnswerResponse(testJobAnswer)).thenReturn(testJobAnswerResponse);

      JobAnswerResponse result = jobAnswerService.updateJobAnswers(testUser, testJobAnswerRequest);

      assertThat(result).isNotNull();
      verify(jobAnswerMapper).updateJobAnswerFromRequest(testJobAnswer, testJobAnswerRequest);
      verify(jobAnswerRepository).save(testJobAnswer);
    }

    @Test
    @DisplayName("should throw exception when job answers not found for update")
    void shouldThrowExceptionWhenJobAnswersNotFoundForUpdate() {
      when(jobAnswerRepository.findByUser(testUser)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> jobAnswerService.updateJobAnswers(testUser, testJobAnswerRequest))
          .isInstanceOf(JobAnswersNotFoundExceptions.class);
    }
  }
}
