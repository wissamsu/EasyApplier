package com.Wissam.EasyApplier.Messaging;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.Wissam.EasyApplier.Config.Kafka.KafkaTopicsProperties;
import com.Wissam.EasyApplier.Messaging.Events.EmailVerificationRequestedEvent;
import com.Wissam.EasyApplier.Messaging.Events.HandshakeJobFoundEvent;
import com.Wissam.EasyApplier.Messaging.Events.HiringCafeJobFoundEvent;
import com.Wissam.EasyApplier.Messaging.Events.LinkedinJobFoundEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final KafkaTopicsProperties topics;

  public void publishEmailVerificationRequested(String email, UUID verificationToken) {
    publish(topics.getEmailVerification(), email, new EmailVerificationRequestedEvent(email, verificationToken));
  }

  public void publishHiringCafeJobFound(
      String jobId,
      String jobTitle,
      String jobImageLink,
      String jobLink,
      String jobLocation,
      String jobCompany,
      UUID userUuid) {
    publish(
        topics.getHiringCafeJobFound(),
        userUuid.toString(),
        new HiringCafeJobFoundEvent(jobId, jobTitle, jobImageLink, jobLink, jobLocation, jobCompany, userUuid));
  }

  public void publishHandshakeJobFound(
      String jobId,
      String jobTitle,
      String jobImageLink,
      String jobLink,
      String jobLocation,
      String jobCompany,
      UUID userUuid) {
    publish(
        topics.getHandshakeJobFound(),
        userUuid.toString(),
        new HandshakeJobFoundEvent(jobId, jobTitle, jobImageLink, jobLink, jobLocation, jobCompany, userUuid));
  }

  public void publishLinkedinJobFound(
      String jobId,
      String jobTitle,
      String jobImageLink,
      String jobLink,
      String jobLocation,
      String jobCompany,
      UUID userUuid) {
    publish(
        topics.getLinkedinJobFound(),
        userUuid.toString(),
        new LinkedinJobFoundEvent(jobId, jobTitle, jobImageLink, jobLink, jobLocation, jobCompany, userUuid));
  }

  private void publish(String topic, String key, Object event) {
    kafkaTemplate.send(topic, key, event).whenComplete((result, ex) -> {
      if (ex != null) {
        log.error("Failed to publish event to topic={} key={}", topic, key, ex);
        return;
      }

      if (result != null) {
        log.info(
            "Published event topic={} partition={} offset={} key={}",
            topic,
            result.getRecordMetadata().partition(),
            result.getRecordMetadata().offset(),
            key);
      }
    });
  }

}
