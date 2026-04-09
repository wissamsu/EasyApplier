package com.Wissam.EasyApplier.Config.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.kafka.autoconfigure.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;

@Configuration
@EnableKafka
@EnableConfigurationProperties(KafkaTopicsProperties.class)
public class KafkaConfig {

  @Bean
  CommonErrorHandler kafkaErrorHandler() {
    DefaultErrorHandler errorHandler = new DefaultErrorHandler(new FixedBackOff(1_000L, 2L));
    errorHandler.addNotRetryableExceptions(
        IllegalArgumentException.class,
        UserNotFoundException.class,
        SerializationException.class,
        TimeoutException.class,
        UnknownTopicOrPartitionException.class);
    return errorHandler;
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
      ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
      ConsumerFactory<Object, Object> consumerFactory,
      CommonErrorHandler kafkaErrorHandler) {
    ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
    configurer.configure(factory, consumerFactory);
    factory.setCommonErrorHandler(kafkaErrorHandler);
    factory.getContainerProperties().setMissingTopicsFatal(false);
    return factory;
  }

  @Bean
  NewTopic emailVerificationTopic(KafkaTopicsProperties topics) {
    return buildTopic(topics.getEmailVerification());
  }

  @Bean
  NewTopic hiringCafeJobFoundTopic(KafkaTopicsProperties topics) {
    return buildTopic(topics.getHiringCafeJobFound());
  }

  @Bean
  NewTopic handshakeJobFoundTopic(KafkaTopicsProperties topics) {
    return buildTopic(topics.getHandshakeJobFound());
  }

  @Bean
  NewTopic linkedinJobFoundTopic(KafkaTopicsProperties topics) {
    return buildTopic(topics.getLinkedinJobFound());
  }

  private NewTopic buildTopic(String topicName) {
    return org.springframework.kafka.config.TopicBuilder.name(topicName)
        .partitions(3)
        .replicas(1)
        .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "1")
        .compact()
        .build();
  }

}
