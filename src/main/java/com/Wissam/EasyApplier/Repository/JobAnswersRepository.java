package com.Wissam.EasyApplier.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Wissam.EasyApplier.Model.JobAnswers;
import com.Wissam.EasyApplier.Model.User;

@Repository
public interface JobAnswersRepository extends JpaRepository<JobAnswers, Long> {

  Optional<JobAnswers> findByUser(User user);

}
