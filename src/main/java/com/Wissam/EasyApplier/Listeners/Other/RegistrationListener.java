package com.Wissam.EasyApplier.Listeners.Other;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Email.JavaMailServiceImpl;
import com.Wissam.EasyApplier.Events.Mail.EmailVerificationEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegistrationListener {

  private final JavaMailServiceImpl javaMailSender;

  @Async("taskExecutor")
  @EventListener
  public void onRegistrationEvent(EmailVerificationEvent event) {
    javaMailSender.sendConfirmSignUpEmail(event.email(), "http://localhost:8080/auth/verify/" + event.uuid());
  }

}
