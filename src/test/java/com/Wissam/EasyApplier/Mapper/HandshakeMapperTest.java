package com.Wissam.EasyApplier.Mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Wissam.EasyApplier.Dto.Handshake.HandshakeRequest;
import com.Wissam.EasyApplier.Dto.Handshake.HandshakeResponse;
import com.Wissam.EasyApplier.Model.Handshake;

@SpringBootTest
@DisplayName("HandshakeMapper Tests")
class HandshakeMapperTest {

  @Autowired
  private HandshakeMapper handshakeMapper;

  private Handshake testHandshake;
  private HandshakeRequest testHandshakeRequest;
  private HandshakeResponse testHandshakeResponse;

  @BeforeEach
  void setUp() {
    testHandshake = Handshake.builder()
        .id(1L)
        .email("handshake@example.com")
        .password("password")
        .build();

    testHandshakeRequest = HandshakeRequest.builder()
        .email("newhandshake@example.com")
        .password("newpassword")
        .build();

    testHandshakeResponse = HandshakeResponse.builder()
        .id(1L)
        .email("handshake@example.com")
        .build();
  }

  @Nested
  @DisplayName("toHandshakeResponse Tests")
  class ToHandshakeResponseTests {

    @Test
    @DisplayName("should convert Handshake to HandshakeResponse")
    void shouldConvertHandshakeToHandshakeResponse() {
      HandshakeResponse result = handshakeMapper.toHandshakeResponse(testHandshake);

      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(testHandshake.getId());
      assertThat(result.getEmail()).isEqualTo(testHandshake.getEmail());
    }

    @Test
    @DisplayName("should return null when Handshake is null")
    void shouldReturnNullWhenHandshakeIsNull() {
      HandshakeResponse result = handshakeMapper.toHandshakeResponse(null);

      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("toHandshake Tests")
  class ToHandshakeTests {

    @Test
    @DisplayName("should convert HandshakeRequest to Handshake")
    void shouldConvertHandshakeRequestToHandshake() {
      Handshake result = handshakeMapper.toHandshake(testHandshakeRequest);

      assertThat(result).isNotNull();
      assertThat(result.getEmail()).isEqualTo(testHandshakeRequest.getEmail());
      assertThat(result.getPassword()).isEqualTo(testHandshakeRequest.getPassword());
    }

    @Test
    @DisplayName("should return null when HandshakeRequest is null")
    void shouldReturnNullWhenHandshakeRequestIsNull() {
      Handshake result = handshakeMapper.toHandshake((HandshakeRequest) null);

      assertThat(result).isNull();
    }
  }

}
