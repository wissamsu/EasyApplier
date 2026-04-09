package com.Wissam.EasyApplier.Config.Kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.kafka.topics")
public class KafkaTopicsProperties {

  private String emailVerification;
  private String hiringCafeJobFound;
  private String handshakeJobFound;
  private String linkedinJobFound;

}
