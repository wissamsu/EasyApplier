package com.Wissam.EasyApplier.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Wissam.EasyApplier.Model.JobInfo;

@Repository
public interface JobInfoRepository extends MongoRepository<JobInfo, ObjectId> {

  List<JobInfo> findAllByJobCompanyName(String jobCompanyName);

  List<JobInfo> findAllByJobLocation(String jobLocation);

  List<JobInfo> findAllByJobName(String jobName);

}
