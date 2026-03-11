package com.Wissam.EasyApplier.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Wissam.EasyApplier.Model.JobInfo;

@Repository
public interface JobInfoRepository extends MongoRepository<JobInfo, Long> {

}
