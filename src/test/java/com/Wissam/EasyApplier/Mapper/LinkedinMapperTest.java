package com.Wissam.EasyApplier.Mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinRequest;
import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinResponse;
import com.Wissam.EasyApplier.Model.Linkedin;

@SpringBootTest
@DisplayName("LinkedinMapper Tests")
class LinkedinMapperTest {

    @Autowired
    private LinkedinMapper linkedinMapper;

    private Linkedin testLinkedin;
    private LinkedinRequest testLinkedinRequest;
    private LinkedinResponse testLinkedinResponse;

    @BeforeEach
    void setUp() {
        testLinkedin = Linkedin.builder()
                .id(1L)
                .email("linkedin@example.com")
                .password("password")
                .liatCookie("li_at=xxxx")
                .build();

        testLinkedinRequest = LinkedinRequest.builder()
                .email("newlinkedin@example.com")
                .password("newpassword")
                .build();

        testLinkedinResponse = LinkedinResponse.builder()
                .id(1L)
                .email("linkedin@example.com")
                .build();
    }

    @Nested
    @DisplayName("toLinkedinResponse Tests")
    class ToLinkedinResponseTests {

        @Test
        @DisplayName("should convert Linkedin to LinkedinResponse")
        void shouldConvertLinkedinToLinkedinResponse() {
            LinkedinResponse result = linkedinMapper.toLinkedinResponse(testLinkedin);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testLinkedin.getId());
            assertThat(result.getEmail()).isEqualTo(testLinkedin.getEmail());
        }

        @Test
        @DisplayName("should return null when Linkedin is null")
        void shouldReturnNullWhenLinkedinIsNull() {
            LinkedinResponse result = linkedinMapper.toLinkedinResponse(null);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("toLinkedin Tests")
    class ToLinkedinTests {

        @Test
        @DisplayName("should convert LinkedinRequest to Linkedin")
        void shouldConvertLinkedinRequestToLinkedin() {
            Linkedin result = linkedinMapper.toLinkedin(testLinkedinRequest);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(testLinkedinRequest.getEmail());
            assertThat(result.getPassword()).isEqualTo(testLinkedinRequest.getPassword());
        }

        @Test
        @DisplayName("should convert LinkedinResponse to Linkedin")
        void shouldConvertLinkedinResponseToLinkedin() {
            Linkedin result = linkedinMapper.toLinkedin(testLinkedinResponse);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testLinkedinResponse.getId());
            assertThat(result.getEmail()).isEqualTo(testLinkedinResponse.getEmail());
        }

        @Test
        @DisplayName("should return null when LinkedinRequest is null")
        void shouldReturnNullWhenLinkedinRequestIsNull() {
            Linkedin result = linkedinMapper.toLinkedin((LinkedinRequest) null);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("toLinkedinRequest Tests")
    class ToLinkedinRequestTests {

        @Test
        @DisplayName("should convert Linkedin to LinkedinRequest")
        void shouldConvertLinkedinToLinkedinRequest() {
            LinkedinRequest result = linkedinMapper.toLinkedinRequest(testLinkedin);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(testLinkedin.getEmail());
            assertThat(result.getPassword()).isEqualTo(testLinkedin.getPassword());
        }

        @Test
        @DisplayName("should return null when Linkedin is null")
        void shouldReturnNullWhenLinkedinIsNull() {
            LinkedinRequest result = linkedinMapper.toLinkedinRequest(null);

            assertThat(result).isNull();
        }
    }
}