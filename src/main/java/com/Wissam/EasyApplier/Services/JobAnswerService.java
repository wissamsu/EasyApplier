package com.Wissam.EasyApplier.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Wissam.EasyApplier.Dto.JobAnswer.JobAnswerRequest;
import com.Wissam.EasyApplier.Dto.JobAnswer.JobAnswerResponse;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.JobAnswersNotFoundExceptions;
import com.Wissam.EasyApplier.Mapper.JobAnswerMapper;
import com.Wissam.EasyApplier.Model.JobAnswer;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.JobAnswersRepository;
import com.Wissam.EasyApplier.Services.IServices.IJobAnswerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobAnswerService implements IJobAnswerService {

  private final JobAnswersRepository jobAnswerRepository;
  private final JobAnswerMapper jobAnswerMapper;

  @Transactional(readOnly = true)
  @Override
  public JobAnswerResponse getJobAnswers(User user) {
    return jobAnswerMapper.toJobAnswerResponse(jobAnswerRepository.findByUser(user).orElseThrow(
        () -> new JobAnswersNotFoundExceptions("Job answers not found for user with email " + user.getEmail())));
  }

  @Transactional
  @Override
  public JobAnswerResponse saveJobAnswers(User user, JobAnswerRequest jobAnswerRequest) {
    JobAnswer jobAnswer = jobAnswerMapper.toJobAnswer(jobAnswerRequest);
    jobAnswer.setUser(user);
    return jobAnswerMapper.toJobAnswerResponse(jobAnswerRepository.save(jobAnswer));
  }

  @Transactional
  @Override
  public JobAnswerResponse updateJobAnswers(User user, JobAnswerRequest jobAnswerRequest) {
    JobAnswer existing = jobAnswerRepository.findByUser(user)
        .orElseThrow(() -> new JobAnswersNotFoundExceptions(
            "Job answers not found for user with email " + user.getEmail()));
    jobAnswerMapper.updateJobAnswerFromRequest(existing, jobAnswerRequest);
    return jobAnswerMapper.toJobAnswerResponse(jobAnswerRepository.save(existing));
  }

}
