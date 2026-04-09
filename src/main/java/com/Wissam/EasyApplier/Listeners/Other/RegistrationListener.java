package com.Wissam.EasyApplier.Listeners.Other;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Email.JavaMailServiceImpl;
import com.Wissam.EasyApplier.Messaging.Events.EmailVerificationRequestedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationListener {

  private final JavaMailServiceImpl javaMailSender;

  @Value("${backend.host.url}")
  private String backendHostUrl;

  @KafkaListener(
      topics = "${app.kafka.topics.email-verification}",
      groupId = "${app.kafka.consumer-groups.email-verification}",
      containerFactory = "kafkaListenerContainerFactory")
  public void onRegistrationEvent(EmailVerificationRequestedEvent event) {
    javaMailSender.sendConfirmSignUpEmail(event.email(), backendHostUrl + "/auth/verify/" + event.verificationToken());
  }

}
