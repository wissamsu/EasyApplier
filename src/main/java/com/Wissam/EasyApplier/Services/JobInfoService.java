package com.Wissam.EasyApplier.Services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Wissam.EasyApplier.Dto.JobInfo.JobInfoResponse;
import com.Wissam.EasyApplier.Mapper.JobInfoMapper;
import com.Wissam.EasyApplier.Model.JobInfo;
import com.Wissam.EasyApplier.Repository.JobInfoRepository;
import com.Wissam.EasyApplier.Services.IServices.IJobInfoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobInfoService implements IJobInfoService {

  private final JobInfoRepository jobInfoRepo;
  private final JobInfoMapper jobInfoMapper;

  @Override
  public List<JobInfoResponse> findAllByJobLocation(String jobLocation) {
    return jobInfoMapper.toJobInfoResponses(jobInfoRepo.findAllByJobLocation(jobLocation));
  }

  @Override
  public List<JobInfoResponse> findAllByJobCompanyName(String jobCompanyName) {
    return jobInfoMapper.toJobInfoResponses(jobInfoRepo.findAllByJobCompanyName(jobCompanyName));
  }

  @Override
  public List<JobInfoResponse> findAllByJobName(String jobName) {
    return jobInfoMapper.toJobInfoResponses(jobInfoRepo.findAllByJobName(jobName));
  }

  @Override
  public List<JobInfoResponse> findAll() {
    return jobInfoMapper.toJobInfoResponses(jobInfoRepo.findAll());
  }

  @Override
  public void save(JobInfo jobInfo) {
    jobInfoRepo.save(jobInfo);
  }

}
