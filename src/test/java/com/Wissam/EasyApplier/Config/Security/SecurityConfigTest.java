package com.Wissam.EasyApplier.Config.Security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@DisplayName("SecurityConfig Tests")
class SecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("PasswordEncoder Tests")
    class PasswordEncoderTests {

        @Test
        @DisplayName("should encode password")
        void shouldEncodePassword() {
            String rawPassword = "testPassword123";
            String encodedPassword = passwordEncoder.encode(rawPassword);

            assertThat(encodedPassword).isNotNull();
            assertThat(encodedPassword).isNotEqualTo(rawPassword);
            assertThat(encodedPassword.startsWith("$2")).isTrue();
        }

        @Test
        @DisplayName("should match raw password with encoded password")
        void shouldMatchRawPasswordWithEncodedPassword() {
            String rawPassword = "testPassword123";
            String encodedPassword = passwordEncoder.encode(rawPassword);

            boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

            assertThat(matches).isTrue();
        }

        @Test
        @DisplayName("should not match different passwords")
        void shouldNotMatchDifferentPasswords() {
            String rawPassword = "testPassword123";
            String wrongPassword = "wrongPassword";
            String encodedPassword = passwordEncoder.encode(rawPassword);

            boolean matches = passwordEncoder.matches(wrongPassword, encodedPassword);

            assertThat(matches).isFalse();
        }

        @Test
        @DisplayName("should generate different encodings for same password")
        void shouldGenerateDifferentEncodingsForSamePassword() {
            String rawPassword = "testPassword123";
            String encodedPassword1 = passwordEncoder.encode(rawPassword);
            String encodedPassword2 = passwordEncoder.encode(rawPassword);

            assertThat(encodedPassword1).isNotEqualTo(encodedPassword2);
            assertThat(passwordEncoder.matches(rawPassword, encodedPassword1)).isTrue();
            assertThat(passwordEncoder.matches(rawPassword, encodedPassword2)).isTrue();
        }
    }

    @Nested
    @DisplayName("PasswordEncoder Type Tests")
    class PasswordEncoderTypeTests {

        @Test
        @DisplayName("should be BCryptPasswordEncoder instance")
        void shouldBeBCryptPasswordEncoderInstance() {
            assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
        }
    }
}