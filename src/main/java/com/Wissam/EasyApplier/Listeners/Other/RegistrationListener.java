package com.Wissam.EasyApplier.Listeners.Other;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Email.JavaMailServiceImpl;
import com.Wissam.EasyApplier.Events.Mail.EmailVerificationEvent;
import com.microsoft.playwright.options.Proxy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationListener {

  private final JavaMailServiceImpl javaMailSender;

  Proxy proxy = new Proxy("http://142.111.48.253:7030")
      .setUsername("jztdgogd")
      .setPassword("94vn6lv3dieu");

  @Async("taskExecutor")
  @EventListener
  public void onRegistrationEvent(EmailVerificationEvent event) {
    javaMailSender.sendConfirmSignUpEmail(event.email(), "http://localhost:8080/auth/verify/" + event.uuid());
  }

}
