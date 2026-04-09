package com.Wissam.EasyApplier.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Wissam.EasyApplier.Dto.Handshake.HandshakeRequest;
import com.Wissam.EasyApplier.Dto.Handshake.HandshakeResponse;
import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.HandshakeNotFoundException;
import com.Wissam.EasyApplier.Mapper.HandshakeMapper;
import com.Wissam.EasyApplier.Model.Handshake;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.HandshakeRepository;
import com.Wissam.EasyApplier.Repository.UserRepository;

@DisplayName("HandshakeService Tests")
@ExtendWith(MockitoExtension.class)
class HandshakeServiceTest {

  @Mock
  private HandshakeMapper handshakeMapper;

  @Mock
  private HandshakeRepository handshakeRepo;

  @Mock
  private UserRepository userRepo;

  @InjectMocks
  private HandshakeService handshakeService;

  private User testUser;
  private Handshake testHandshake;
  private HandshakeRequest testHandshakeRequest;
  private HandshakeResponse testHandshakeResponse;

  @BeforeEach
  void setUp() {
    testUser = User.builder()
        .id(1L)
        .email("test@example.com")
        .password("password")
        .role(UserRole.ROLE_USER)
        .verified(true)
        .build();

    testHandshake = Handshake.builder()
        .id(1L)
        .email("handshake@example.com")
        .password("handshakePassword")
        .user(testUser)
        .build();

    testHandshakeRequest = HandshakeRequest.builder()
        .email("handshake@example.com")
        .password("handshakePassword")
        .build();

    testHandshakeResponse = HandshakeResponse.builder()
        .id(1L)
        .email("handshake@example.com")
        .password("handshakePassword")
        .build();
  }

  @Nested
  @DisplayName("getHandshake Tests")
  class GetHandshakeTests {

    @Test
    @DisplayName("should return handshake response for authenticated user")
    void shouldReturnHandshakeResponseForAuthenticatedUser() {
      testUser.setHandshake(testHandshake);
      when(handshakeMapper.toHandshakeResponse(testHandshake)).thenReturn(testHandshakeResponse);

      HandshakeResponse result = handshakeService.getHandshake(testUser);

      assertThat(result).isNotNull();
      assertThat(result.getEmail()).isEqualTo("handshake@example.com");
      verify(handshakeMapper).toHandshakeResponse(testHandshake);
    }
  }

  @Nested
  @DisplayName("getHandshakeByEmail Tests")
  class GetHandshakeByEmailTests {

    @Test
    @DisplayName("should return handshake response when email exists")
    void shouldReturnHandshakeResponseWhenEmailExists() {
      when(handshakeRepo.findByEmail("handshake@example.com")).thenReturn(Optional.of(testHandshake));
      when(handshakeMapper.toHandshakeResponse(testHandshake)).thenReturn(testHandshakeResponse);

      HandshakeResponse result = handshakeService.getHandshakeByEmail("handshake@example.com");

      assertThat(result).isNotNull();
      assertThat(result.getEmail()).isEqualTo("handshake@example.com");
    }

    @Test
    @DisplayName("should throw HandshakeNotFoundException when email not found")
    void shouldThrowHandshakeNotFoundExceptionWhenEmailNotFound() {
      when(handshakeRepo.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

      assertThatThrownBy(() -> handshakeService.getHandshakeByEmail("nonexistent@example.com"))
          .isInstanceOf(HandshakeNotFoundException.class)
          .hasMessageContaining("not found");
    }
  }

  @Nested
  @DisplayName("createHandshake Tests")
  class CreateHandshakeTests {

    @Test
    @DisplayName("should create handshake successfully")
    void shouldCreateHandshakeSuccessfully() {
      when(handshakeMapper.toHandshake(testHandshakeRequest)).thenReturn(testHandshake);
      when(userRepo.save(any(User.class))).thenReturn(testUser);
      when(handshakeMapper.toHandshakeResponse(testHandshake)).thenReturn(testHandshakeResponse);

      HandshakeResponse result = handshakeService.createHandshake(testHandshakeRequest, testUser);

      assertThat(result).isNotNull();
      verify(handshakeMapper).toHandshake(testHandshakeRequest);
      verify(userRepo).save(testUser);
    }

    @Test
    @DisplayName("should set user on handshake when creating")
    void shouldSetUserOnHandshakeWhenCreating() {
      when(handshakeMapper.toHandshake(testHandshakeRequest)).thenReturn(testHandshake);
      when(userRepo.save(any(User.class))).thenReturn(testUser);
      when(handshakeMapper.toHandshakeResponse(any(Handshake.class))).thenReturn(testHandshakeResponse);

      handshakeService.createHandshake(testHandshakeRequest, testUser);

      assertThat(testHandshake.getUser()).isEqualTo(testUser);
      assertThat(testUser.getHandshake()).isEqualTo(testHandshake);
    }
  }

  @Nested
  @DisplayName("updateHandshake Tests")
  class UpdateHandshakeTests {

    @Test
    @DisplayName("should update handshake successfully")
    void shouldUpdateHandshakeSuccessfully() {
      testUser.setHandshake(testHandshake);
      HandshakeRequest updateRequest = HandshakeRequest.builder()
          .email("updated@example.com")
          .password("newPassword")
          .build();

      when(handshakeRepo.save(any(Handshake.class))).thenReturn(testHandshake);
      when(handshakeMapper.toHandshakeResponse(testHandshake)).thenReturn(testHandshakeResponse);

      HandshakeResponse result = handshakeService.updateHandshake(updateRequest, testUser);

      assertThat(result).isNotNull();
      verify(handshakeMapper).updateHandshakeFromRequest(testHandshake, updateRequest);
      verify(handshakeRepo).save(testHandshake);
    }
  }
}
