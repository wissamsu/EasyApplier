package com.Wissam.EasyApplier.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByEmailAndPassword(String email, String password);

  Optional<User> findByUuid(UUID uuid);

  List<User> findAllByRole(UserRole role);

  Optional<User> findUserByLinkedin_Id(Long id);

  Optional<User> findUserByLinkedin_Email(String email);

}
