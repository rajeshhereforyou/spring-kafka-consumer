package com.example.kafka.consumerretry;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class FactoryConfigurer {

  FactoryConfigurer(ConcurrentKafkaListenerContainerFactory<?, ?> factory) {
    RetryTemplate retryTemplate = new RetryTemplate();
    retryTemplate.setRetryPolicy(new AlwaysRetryPolicy());
    FixedBackOffPolicy backOff = new FixedBackOffPolicy();
    backOff.setBackOffPeriod(1000);
    retryTemplate.setBackOffPolicy(backOff);
    factory.setRetryTemplate(retryTemplate);

    factory.setStatefulRetry(true);
  }

}
