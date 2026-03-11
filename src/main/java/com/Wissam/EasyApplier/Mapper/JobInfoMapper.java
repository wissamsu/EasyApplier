package com.Wissam.EasyApplier.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.Wissam.EasyApplier.Dto.JobInfo.JobInfoRequest;
import com.Wissam.EasyApplier.Dto.JobInfo.JobInfoResponse;
import com.Wissam.EasyApplier.Model.JobInfo;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface JobInfoMapper {

  JobInfo toJobInfo(JobInfoResponse jobInfoResponse);

  JobInfoResponse toJobInfoResponse(JobInfo jobInfo);

  JobInfoRequest toJobInfoRequest(JobInfo jobInfo);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "appliedUsers", ignore = true)
  JobInfo toJobInfo(JobInfoRequest jobInfoRequest);

  List<JobInfoResponse> toJobInfoResponses(List<JobInfo> jobInfos);

}
