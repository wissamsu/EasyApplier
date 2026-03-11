package com.Wissam.EasyApplier.Services;

import org.springframework.stereotype.Service;

import com.Wissam.EasyApplier.Repository.JobInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobInfoService {

  private final JobInfoRepository jobInfoRepo;

}
