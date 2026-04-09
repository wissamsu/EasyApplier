package com.Wissam.EasyApplier.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  Optional<User> findByUuid(UUID uuid);

  @Query("""
      select u
      from User u
      left join fetch u.linkedin
      left join fetch u.handshake
      left join fetch u.jobAnswer
      where u.uuid = :uuid
      """)
  Optional<User> findAutomationUserByUuid(@Param("uuid") UUID uuid);

  List<User> findAllByRole(UserRole role);

  Optional<User> findUserByLinkedin_Id(Long id);

  Optional<User> findUserByLinkedin_Email(String email);

}
