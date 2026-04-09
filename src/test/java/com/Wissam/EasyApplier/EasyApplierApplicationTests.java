package com.Wissam.EasyApplier;

import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EasyApplier Core Tests")
class EasyApplierApplicationTests {

    @Nested
    @DisplayName("UserRole Enum Tests")
    class UserRoleTests {

        @Test
        @DisplayName("should have ROLE_USER value")
        void shouldHaveRoleUserValue() {
            assertThat(UserRole.ROLE_USER).isNotNull();
            assertThat(UserRole.ROLE_USER.name()).isEqualTo("ROLE_USER");
        }

        @Test
        @DisplayName("should have ROLE_ADMIN value")
        void shouldHaveRoleAdminValue() {
            assertThat(UserRole.ROLE_ADMIN).isNotNull();
            assertThat(UserRole.ROLE_ADMIN.name()).isEqualTo("ROLE_ADMIN");
        }

        @Test
        @DisplayName("should have exactly two values")
        void shouldHaveExactlyTwoValues() {
            assertThat(UserRole.values()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Exception Tests")
    class ExceptionTests {

        @Test
        @DisplayName("UserNotFoundException should contain correct message")
        void userNotFoundExceptionShouldContainCorrectMessage() {
            String message = "User with email test@example.com not found";
            UserNotFoundException exception = new UserNotFoundException(message);
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("LinkedinNotFoundException should contain correct message")
        void linkedinNotFoundExceptionShouldContainCorrectMessage() {
            String message = "Linkedin with id 1 not found";
            LinkedinNotFoundException exception = new LinkedinNotFoundException(message);
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("HandshakeNotFoundException should contain correct message")
        void handshakeNotFoundExceptionShouldContainCorrectMessage() {
            String message = "Handshake with email test@example.com not found";
            HandshakeNotFoundException exception = new HandshakeNotFoundException(message);
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("JobAnswersNotFoundExceptions should contain correct message")
        void jobAnswersNotFoundExceptionsShouldContainCorrectMessage() {
            String message = "Job answers not found for user";
            JobAnswersNotFoundExceptions exception = new JobAnswersNotFoundExceptions(message);
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("JobInfoInvalidException should contain correct message")
        void jobInfoInvalidExceptionShouldContainCorrectMessage() {
            String message = "Invalid job info";
            JobInfoInvalidException exception = new JobInfoInvalidException(message);
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("LiAtCookieInvalidException should contain correct message")
        void liAtCookieInvalidExceptionShouldContainCorrectMessage() {
            String message = "Li_at cookie is invalid";
            LiAtCookieInvalidException exception = new LiAtCookieInvalidException(message);
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("QuestionOrIdNotFoundException should contain correct message")
        void questionOrIdNotFoundExceptionShouldContainCorrectMessage() {
            String message = "Question or ID not found";
            QuestionOrIdNotFoundException exception = new QuestionOrIdNotFoundException(message);
            assertThat(exception.getMessage()).isEqualTo(message);
        }
    }
}
