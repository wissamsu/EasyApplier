package com.Wissam.EasyApplier.Services.IServices;

import com.Wissam.EasyApplier.Dto.JobAnswer.JobAnswerRequest;
import com.Wissam.EasyApplier.Dto.JobAnswer.JobAnswerResponse;
import com.Wissam.EasyApplier.Model.User;

public interface IJobAnswerService {

  public JobAnswerResponse getJobAnswers(User user);

  public JobAnswerResponse saveJobAnswers(User user, JobAnswerRequest jobAnswerRequest);

  public JobAnswerResponse updateJobAnswers(User user, JobAnswerRequest jobAnswerRequest);

}
