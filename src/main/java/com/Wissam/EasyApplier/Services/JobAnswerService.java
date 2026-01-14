package com.Wissam.EasyApplier.Services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.QuestionOrIdNotFoundException;
import com.Wissam.EasyApplier.Model.JobAnswer;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.JobAnswersRepository;
import com.Wissam.EasyApplier.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobAnswerService {

  private final JobAnswersRepository jobAnswerRepository;
  private final UserRepository userRepository;

  @Transactional
  public JobAnswer addOrUpdateAnswer(Long userId, String question, String answer) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    JobAnswer jobAnswer = jobAnswerRepository.findByUserIdAndQuestion(userId, question)
        .orElse(JobAnswer.builder()
            .user(user)
            .question(question)
            .build());

    jobAnswer.setAnswer(answer);

    return jobAnswerRepository.save(jobAnswer);
  }

  @Transactional(readOnly = true)
  public List<JobAnswer> getAllAnswersForUser(Long userId) {
    return jobAnswerRepository.findByUserId(userId);
  }

  @Transactional(readOnly = true)
  public JobAnswer getAnswerForQuestion(Long userId, String question) {
    return jobAnswerRepository.findByUserIdAndQuestion(userId, question)
        .orElseThrow(() -> new QuestionOrIdNotFoundException("Question or id not found"));
  }

  @Transactional
  public void deleteAnswer(Long userId, String question) {
    jobAnswerRepository.findByUserIdAndQuestion(userId, question)
        .ifPresent(jobAnswerRepository::delete);
  }
}
