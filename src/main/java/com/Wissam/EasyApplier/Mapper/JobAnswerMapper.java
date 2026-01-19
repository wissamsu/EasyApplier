package com.Wissam.EasyApplier.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.Wissam.EasyApplier.Dto.JobAnswer.JobAnswerRequest;
import com.Wissam.EasyApplier.Dto.JobAnswer.JobAnswerResponse;
import com.Wissam.EasyApplier.Model.JobAnswer;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface JobAnswerMapper {

  JobAnswerResponse toJobAnswerResponse(JobAnswer jobAnswer);

  JobAnswerRequest toJobAnswerRequest(JobAnswer jobAnswer);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  JobAnswer toJobAnswer(JobAnswerRequest jobAnswerRequest);

  @Mapping(target = "user", ignore = true)
  JobAnswer toJobAnswer(JobAnswerResponse jobAnswerResponse);

  List<JobAnswerResponse> toJobAnswerResponseList(List<JobAnswer> jobAnswers);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  void updateJobAnswerFromRequest(@MappingTarget JobAnswer jobAnswer, JobAnswerRequest jobAnswerRequest);

}
