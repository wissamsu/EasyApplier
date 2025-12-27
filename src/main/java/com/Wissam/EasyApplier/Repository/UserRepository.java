package com.Wissam.EasyApplier.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Wissam.EasyApplier.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByEmailAndPassword(String email, String password);

  Optional<User> findByUuid(String uuid);

}
