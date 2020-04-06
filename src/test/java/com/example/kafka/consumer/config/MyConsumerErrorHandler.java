package com.example.kafka.consumer.config;

import com.example.kafka.consumer.retry.ConsumerRetryApplication;
import java.util.List;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.stereotype.Component;

@Component
public class MyConsumerErrorHandler extends SeekToCurrentErrorHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ConsumerRetryApplication.class);

  MyConsumerErrorHandler() {
    super(5);
  }

  @Override
  public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer,
      MessageListenerContainer container) {

    LOG.info("handle");
    super.handle(thrownException, records, consumer, container);
  }
}
