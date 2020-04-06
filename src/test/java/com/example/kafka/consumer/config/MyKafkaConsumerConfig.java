package com.example.kafka.consumer.config;

import com.example.kafka.MyKafkaAbstractConfig;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class MyKafkaConsumerConfig extends MyKafkaAbstractConfig {

  private static final Logger LOG = LoggerFactory.getLogger(MyKafkaConsumerConfig.class);

  private final MyConsumerErrorHandler errorHandler;

  @Autowired
  public MyKafkaConsumerConfig(MyConsumerErrorHandler errorHandler){
    this.errorHandler = errorHandler;
    System.out.println(" $$$$$$$$$$$ "+embedded.getEmbeddedKafka().getBrokersAsString());
  }

  @Bean
  public <K,V> ConcurrentKafkaListenerContainerFactory<String, String> factory(ConcurrentKafkaListenerContainerFactory<String, String> factory) {
    //ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(defaultKafkaConsumerFactory());

    factory.getContainerProperties().setMissingTopicsFatal(false);
    factory.setRetryTemplate(retryTemplate());
    factory.setStatefulRetry(true);
    return factory;
  }

  public DefaultKafkaConsumerFactory defaultKafkaConsumerFactory(){
    Map<String, Object> consumerProperties = getConsumerConfigs("testGroup", "false");
    DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<String, String>(consumerProperties);

    return consumerFactory;
  }

  public static Map<String, Object> getConsumerConfigs( String group, String autoCommit) {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embedded.getEmbeddedKafka().getBrokersAsString());
    props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
    //props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10");
    //props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "60000");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    return props;
  }

  public RetryTemplate retryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();
    retryTemplate.setRetryPolicy(new AlwaysRetryPolicy());

    FixedBackOffPolicy backOff = new FixedBackOffPolicy();
    backOff.setBackOffPeriod(1000);
    retryTemplate.setBackOffPolicy(backOff);

    return retryTemplate;
  }

}
