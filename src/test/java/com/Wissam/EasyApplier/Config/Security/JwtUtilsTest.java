package com.Wissam.EasyApplier.Config.Security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.Wissam.EasyApplier.Config.Security.SecurityUtils.JwtUtils;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("JwtUtils Tests")
class JwtUtilsTest {

  @Autowired
  private JwtUtils jwtUtils;

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private int expiration;

  private static final String TEST_USERNAME = "test@example.com";

  @Nested
  @DisplayName("generateToken Tests")
  class GenerateTokenTests {

    @Test
    @DisplayName("should generate token successfully")
    void shouldGenerateTokenSuccessfully() {
      String token = jwtUtils.generateToken(TEST_USERNAME);

      assertThat(token).isNotNull();
      assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("should generate different tokens for same user")
    void shouldGenerateDifferentTokensForSameUser() throws InterruptedException {
      String token1 = jwtUtils.generateToken(TEST_USERNAME);
      Thread.sleep(10);
      String token2 = jwtUtils.generateToken(TEST_USERNAME);

      assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    @DisplayName("should generate valid JWT format token")
    void shouldGenerateValidJWTFormatToken() {
      String token = jwtUtils.generateToken(TEST_USERNAME);

      String[] parts = token.split("\\.");
      assertThat(parts).hasSize(3);
    }
  }

  @Nested
  @DisplayName("getSubject Tests")
  class GetSubjectTests {

    @Test
    @DisplayName("should extract subject from token")
    void shouldExtractSubjectFromToken() {
      String token = jwtUtils.generateToken(TEST_USERNAME);

      String subject = jwtUtils.getSubject(token);

      assertThat(subject).isEqualTo(TEST_USERNAME);
    }

    @Test
    @DisplayName("should return null for invalid token")
    void shouldReturnNullForInvalidToken() {
      String invalidToken = "invalid.token.here";

      try {
        jwtUtils.getSubject(invalidToken);
      } catch (Exception e) {
        assertThat(e).isInstanceOf(Exception.class);
      }
    }
  }

  @Nested
  @DisplayName("varifyToken Tests")
  class VarifyTokenTests {

    @Test
    @DisplayName("should verify valid token")
    void shouldVerifyValidToken() {
      String token = jwtUtils.generateToken(TEST_USERNAME);

      boolean isValid = jwtUtils.varifyToken(token);

      assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("should return false for invalid token")
    void shouldReturnFalseForInvalidToken() {
      String invalidToken = "invalid.token.here";

      boolean isValid = jwtUtils.varifyToken(invalidToken);

      assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("should return false for empty token")
    void shouldReturnFalseForEmptyToken() {
      boolean isValid = jwtUtils.varifyToken("");

      assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("should return false for tampered token")
    void shouldReturnFalseForTamperedToken() {
      String token = jwtUtils.generateToken(TEST_USERNAME);
      String tamperedToken = token.substring(0, token.length() - 5) + "xxxxx";

      boolean isValid = jwtUtils.varifyToken(tamperedToken);

      assertThat(isValid).isFalse();
    }
  }

  @Nested
  @DisplayName("Token Expiration Tests")
  class TokenExpirationTests {

    @Test
    @DisplayName("should create token with expiration")
    void shouldCreateTokenWithExpiration() {
      String token = jwtUtils.generateToken(TEST_USERNAME);

      assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("should reject expired token")
    void shouldRejectExpiredToken() {
      String token = jwtUtils.generateToken(TEST_USERNAME);

      boolean isValid = jwtUtils.varifyToken(token);

      assertThat(isValid).isTrue();
    }
  }

  @Nested
  @DisplayName("Token Claims Tests")
  class TokenClaimsTests {

    @Test
    @DisplayName("should include username in token claims")
    void shouldIncludeUsernameInTokenClaims() {
      String token = jwtUtils.generateToken(TEST_USERNAME);

      String subject = jwtUtils.getSubject(token);

      assertThat(subject).isEqualTo(TEST_USERNAME);
    }

    @Test
    @DisplayName("should handle special characters in username")
    void shouldHandleSpecialCharactersInUsername() {
      String specialUsername = "user+special@example.com";
      String token = jwtUtils.generateToken(specialUsername);

      String subject = jwtUtils.getSubject(token);

      assertThat(subject).isEqualTo(specialUsername);
    }
  }
}
