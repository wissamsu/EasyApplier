package com.Wissam.EasyApplier.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.UserRepository;

@DisplayName("AuthService Tests")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .role(UserRole.ROLE_USER)
                .verified(true)
                .build();
    }

    @Nested
    @DisplayName("login Tests")
    class LoginTests {

        @Test
        @DisplayName("should return true when login is successful")
        void shouldReturnTrueWhenLoginIsSuccessful() {
            when(userRepo.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

            Authentication authentication = mock(Authentication.class);
            when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

            boolean result = authService.login(testUser.getEmail(), "password");

            assertThat(result).isTrue();
            verify(userRepo).findByEmail(testUser.getEmail());
        }

        @Test
        @DisplayName("should return false when password is invalid")
        void shouldReturnFalseWhenPasswordIsInvalid() {
            when(userRepo.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
            when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));

            boolean result = authService.login(testUser.getEmail(), "wrongPassword");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("should return false when user is not verified")
        void shouldReturnFalseWhenUserNotVerified() {
            testUser.setVerified(false);
            when(userRepo.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

            boolean result = authService.login(testUser.getEmail(), "password");

            assertThat(result).isFalse();
            verify(authManager, never()).authenticate(any());
        }

        @Test
        @DisplayName("should return false when user not found")
        void shouldReturnFalseWhenUserNotFound() {
            when(userRepo.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

            boolean result = authService.login("nonexistent@example.com", "password");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("should throw exception when authentication fails")
        void shouldThrowExceptionWhenAuthenticationFails() {
            when(userRepo.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
            when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));

            boolean result = authService.login(testUser.getEmail(), "password");

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("register Tests")
    class RegisterTests {

        @Test
        @DisplayName("should register user successfully")
        void shouldRegisterUserSuccessfully() {
            UUID uuid = UUID.randomUUID();
            when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
            when(userRepo.save(any(User.class))).thenReturn(testUser);

            String result = authService.register(testUser.getEmail(), "password", uuid);

            assertThat(result).isEqualTo("User registered successfully");
            verify(passwordEncoder).encode("password");
            verify(userRepo).save(any(User.class));
        }

        @Test
        @DisplayName("should encode password during registration")
        void shouldEncodePasswordDuringRegistration() {
            UUID uuid = UUID.randomUUID();
            when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
            when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            authService.register(testUser.getEmail(), "password", uuid);

            verify(passwordEncoder).encode("password");
        }
    }

    @Nested
    @DisplayName("verifyUser Tests")
    class VerifyUserTests {

        @Test
        @DisplayName("should verify user successfully")
        void shouldVerifyUserSuccessfully() {
            testUser.setVerified(false);
            when(userRepo.save(any(User.class))).thenReturn(testUser);

            authService.verifyUser(testUser);

            assertThat(testUser.isVerified()).isTrue();
            verify(userRepo).save(testUser);
        }
    }
}
