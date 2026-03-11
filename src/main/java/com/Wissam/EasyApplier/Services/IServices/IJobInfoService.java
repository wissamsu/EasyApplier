package com.Wissam.EasyApplier.Services.IServices;

import java.util.List;

import com.Wissam.EasyApplier.Dto.JobInfo.JobInfoResponse;
import com.Wissam.EasyApplier.Model.JobInfo;

public interface IJobInfoService {

  public List<JobInfoResponse> findAllByJobLocation(String jobLocation);

  public List<JobInfoResponse> findAllByJobCompanyName(String jobCompanyName);

  public List<JobInfoResponse> findAllByJobName(String jobName);

  public List<JobInfoResponse> findAll();

  public void save(JobInfo jobInfo);

}
