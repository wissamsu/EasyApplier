package com.Wissam.EasyApplier.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinRequest;
import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinResponse;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.LinkedinNotFoundException;
import com.Wissam.EasyApplier.Mapper.LinkedinMapper;
import com.Wissam.EasyApplier.Model.Linkedin;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.LinkedinRepository;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.Wissam.EasyApplier.Enums.UserRole;

@DisplayName("LinkedinService Tests")
@ExtendWith(MockitoExtension.class)
class LinkedinServiceTest {

    @Mock
    private LinkedinRepository linkedinRepo;

    @Mock
    private LinkedinMapper linkedinMapper;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private LinkedinService linkedinService;

    private User testUser;
    private Linkedin testLinkedin;
    private LinkedinRequest testLinkedinRequest;
    private LinkedinResponse testLinkedinResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .role(UserRole.ROLE_USER)
                .verified(true)
                .build();

        testLinkedin = Linkedin.builder()
                .id(1L)
                .email("linkedin@example.com")
                .password("linkedinPassword")
                .liatCookie("li_at=xxxx")
                .user(testUser)
                .build();

        testLinkedinRequest = LinkedinRequest.builder()
                .email("linkedin@example.com")
                .password("linkedinPassword")
                .build();

        testLinkedinResponse = LinkedinResponse.builder()
                .id(1L)
                .email("linkedin@example.com")
                .build();
    }

    @Nested
    @DisplayName("getLinkedinById Tests")
    class GetLinkedinByIdTests {

        @Test
        @DisplayName("should return linkedin response when id exists")
        void shouldReturnLinkedinResponseWhenIdExists() {
            when(linkedinRepo.findById(1L)).thenReturn(Optional.of(testLinkedin));
            when(linkedinMapper.toLinkedinResponse(testLinkedin)).thenReturn(testLinkedinResponse);

            LinkedinResponse result = linkedinService.getLinkedinById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getEmail()).isEqualTo("linkedin@example.com");
            verify(linkedinRepo).findById(1L);
        }

        @Test
        @DisplayName("should throw LinkedinNotFoundException when id not found")
        void shouldThrowLinkedinNotFoundExceptionWhenIdNotFound() {
            when(linkedinRepo.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> linkedinService.getLinkedinById(999L))
                    .isInstanceOf(LinkedinNotFoundException.class)
                    .hasMessageContaining("not found");
        }
    }

    @Nested
    @DisplayName("getLinkedinByEmail Tests")
    class GetLinkedinByEmailTests {

        @Test
        @DisplayName("should return linkedin response when email exists")
        void shouldReturnLinkedinResponseWhenEmailExists() {
            when(linkedinRepo.findByEmail("linkedin@example.com")).thenReturn(Optional.of(testLinkedin));
            when(linkedinMapper.toLinkedinResponse(testLinkedin)).thenReturn(testLinkedinResponse);

            LinkedinResponse result = linkedinService.getLinkedinByEmail("linkedin@example.com");

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("linkedin@example.com");
        }

        @Test
        @DisplayName("should throw LinkedinNotFoundException when email not found")
        void shouldThrowLinkedinNotFoundExceptionWhenEmailNotFound() {
            when(linkedinRepo.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> linkedinService.getLinkedinByEmail("nonexistent@example.com"))
                    .isInstanceOf(LinkedinNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getLinkedin Tests")
    class GetLinkedinTests {

        @Test
        @DisplayName("should return current users linkedin profile")
        void shouldReturnCurrentUsersLinkedinProfile() {
            testUser.setLinkedin(testLinkedin);
            when(linkedinMapper.toLinkedinResponse(testLinkedin)).thenReturn(testLinkedinResponse);

            LinkedinResponse result = linkedinService.getLinkedin(testUser);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("linkedin@example.com");
        }

        @Test
        @DisplayName("should throw when current user has no linkedin profile")
        void shouldThrowWhenCurrentUserHasNoLinkedinProfile() {
            assertThatThrownBy(() -> linkedinService.getLinkedin(testUser))
                    .isInstanceOf(LinkedinNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("createLinkedin Tests")
    class CreateLinkedinTests {

        @Test
        @DisplayName("should create linkedin successfully")
        void shouldCreateLinkedinSuccessfully() {
            when(linkedinMapper.toLinkedin(testLinkedinRequest)).thenReturn(testLinkedin);
            when(userRepo.save(any(User.class))).thenReturn(testUser);
            when(linkedinMapper.toLinkedinResponse(testLinkedin)).thenReturn(testLinkedinResponse);

            LinkedinResponse result = linkedinService.createLinkedin(testLinkedinRequest, testUser);

            assertThat(result).isNotNull();
            verify(linkedinMapper).toLinkedin(testLinkedinRequest);
            verify(userRepo).save(testUser);
        }

        @Test
        @DisplayName("should set user on linkedin when creating")
        void shouldSetUserOnLinkedinWhenCreating() {
            when(linkedinMapper.toLinkedin(testLinkedinRequest)).thenReturn(testLinkedin);
            when(userRepo.save(any(User.class))).thenReturn(testUser);
            when(linkedinMapper.toLinkedinResponse(any(Linkedin.class))).thenReturn(testLinkedinResponse);

            linkedinService.createLinkedin(testLinkedinRequest, testUser);

            assertThat(testLinkedin.getUser()).isEqualTo(testUser);
            assertThat(testUser.getLinkedin()).isEqualTo(testLinkedin);
        }
    }

    @Nested
    @DisplayName("addLiAtCookie Tests")
    class AddLiAtCookieTests {

        @Test
        @DisplayName("should add li_at cookie successfully")
        void shouldAddLiAtCookieSuccessfully() {
            testUser.setLinkedin(testLinkedin);
            when(linkedinRepo.save(any(Linkedin.class))).thenReturn(testLinkedin);
            when(linkedinMapper.toLinkedinResponse(testLinkedin)).thenReturn(testLinkedinResponse);

            LinkedinResponse result = linkedinService.addLi_AtCookie(testUser, "li_at=newcookie");

            assertThat(result).isNotNull();
            assertThat(testLinkedin.getLiatCookie()).isEqualTo("li_at=newcookie");
            verify(linkedinRepo).save(testLinkedin);
        }

        @Test
        @DisplayName("should update existing cookie value")
        void shouldUpdateExistingCookieValue() {
            testUser.setLinkedin(testLinkedin);
            when(linkedinRepo.save(any(Linkedin.class))).thenReturn(testLinkedin);
            when(linkedinMapper.toLinkedinResponse(any(Linkedin.class))).thenReturn(testLinkedinResponse);

            linkedinService.addLi_AtCookie(testUser, "li_at=updatedCookie");

            assertThat(testLinkedin.getLiatCookie()).isEqualTo("li_at=updatedCookie");
        }
    }
}
