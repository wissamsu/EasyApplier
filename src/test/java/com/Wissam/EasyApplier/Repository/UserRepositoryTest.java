package com.Wissam.EasyApplier.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Model.Linkedin;
import com.Wissam.EasyApplier.Model.User;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("UserRepository Tests")
class UserRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  private User testUser;
  private Linkedin testLinkedin;

  @BeforeEach
  void setUp() {
    testLinkedin = Linkedin.builder()
        .email("linkedin@example.com")
        .password("password")
        .build();

    testUser = User.builder()
        .email("test@example.com")
        .password("encodedPassword")
        .role(UserRole.ROLE_USER)
        .verified(true)
        .uuid(UUID.randomUUID())
        .build();
  }

  @Nested
  @DisplayName("findByEmail Tests")
  class FindByEmailTests {

    @Test
    @DisplayName("should find user by email")
    void shouldFindUserByEmail() {
      entityManager.persist(testUser);
      entityManager.flush();

      Optional<User> result = userRepository.findByEmail("test@example.com");

      assertThat(result).isPresent();
      assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("should return empty when email not found")
    void shouldReturnEmptyWhenEmailNotFound() {
      Optional<User> result = userRepository.findByEmail("nonexistent@example.com");

      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("existsByEmail Tests")
  class ExistsByEmailTests {

    @Test
    @DisplayName("should return true when email exists")
    void shouldReturnTrueWhenEmailExists() {
      entityManager.persist(testUser);
      entityManager.flush();

      boolean result = userRepository.existsByEmail("test@example.com");

      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should return false when email does not exist")
    void shouldReturnFalseWhenEmailDoesNotExist() {
      boolean result = userRepository.existsByEmail("nonexistent@example.com");

      assertThat(result).isFalse();
    }
  }

  @Nested
  @DisplayName("findByUuid Tests")
  class FindByUuidTests {

    @Test
    @DisplayName("should find user by uuid")
    void shouldFindUserByUuid() {
      UUID uuid = UUID.randomUUID();
      testUser.setUuid(uuid);
      entityManager.persist(testUser);
      entityManager.flush();

      Optional<User> result = userRepository.findByUuid(uuid);

      assertThat(result).isPresent();
      assertThat(result.get().getUuid()).isEqualTo(uuid);
    }
  }

  @Nested
  @DisplayName("findAllByRole Tests")
  class FindAllByRoleTests {

    @Test
    @DisplayName("should find all users by role")
    void shouldFindAllUsersByRole() {
      User adminUser = User.builder()
          .email("admin@example.com")
          .password("password")
          .role(UserRole.ROLE_ADMIN)
          .verified(true)
          .build();

      entityManager.persist(testUser);
      entityManager.persist(adminUser);
      entityManager.flush();

      List<User> result = userRepository.findAllByRole(UserRole.ROLE_USER);

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getRole()).isEqualTo(UserRole.ROLE_USER);
    }
  }

  @Nested
  @DisplayName("findUserByLinkedin_Id Tests")
  class FindUserByLinkedinIdTests {

    @Test
    @DisplayName("should find user by linkedin id")
    void shouldFindUserByLinkedinId() {
      entityManager.persist(testLinkedin);
      entityManager.flush();

      testUser.setLinkedin(testLinkedin);
      entityManager.persist(testUser);
      entityManager.flush();

      Optional<User> result = userRepository.findUserByLinkedin_Id(testLinkedin.getId());

      assertThat(result).isPresent();
      assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }
  }

  @Nested
  @DisplayName("findUserByLinkedin_Email Tests")
  class FindUserByLinkedinEmailTests {

    @Test
    @DisplayName("should find user by linkedin email")
    void shouldFindUserByLinkedinEmail() {
      entityManager.persist(testLinkedin);
      entityManager.flush();

      testUser.setLinkedin(testLinkedin);
      entityManager.persist(testUser);
      entityManager.flush();

      Optional<User> result = userRepository.findUserByLinkedin_Email("linkedin@example.com");

      assertThat(result).isPresent();
      assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }
  }
}
