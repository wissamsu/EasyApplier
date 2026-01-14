package com.Wissam.EasyApplier.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Wissam.EasyApplier.Model.JobAnswer;
import com.Wissam.EasyApplier.Model.User;

@Repository
public interface JobAnswersRepository extends JpaRepository<JobAnswer, Long> {

  Optional<JobAnswer> findByUser(User user);

  List<JobAnswer> findByUserId(Long userId);

  Optional<JobAnswer> findByUserIdAndQuestion(Long userId, String question);

}
