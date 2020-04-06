package com.example.kafka;

import org.junit.ClassRule;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;

public class MyKafkaAbstractConfig {

  @ClassRule
  public static EmbeddedKafkaRule embedded = new EmbeddedKafkaRule(1);

}
