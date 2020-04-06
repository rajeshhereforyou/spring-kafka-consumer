package com.example.kafka.consumerretry;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ConsumerRetryApplication {

	private static final Logger LOG = LoggerFactory.getLogger(ConsumerRetryApplication.class);
	private final AtomicInteger count = new AtomicInteger();

	public static void main(String[] args) {
		SpringApplication.run(ConsumerRetryApplication.class, args);
	}

	@KafkaListener(id = "123", topics = "testTopic")
	public void listen(String in) {
		LOG.info(in + this.count.incrementAndGet());
		throw new RuntimeException();
	}

	@Bean
	public ErrorHandler eh() {
		class MyEH extends SeekToCurrentErrorHandler {

			MyEH() {
				super(-1);
			}

			@Override
			public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer,
					MessageListenerContainer container) {

				LOG.info("handle");
				super.handle(thrownException, records, consumer, container);
			}

		};
		return new MyEH();
	}

	@Bean
	public NewTopic topic() {
		return new NewTopic("testTopic", 1, (short) 1);
	}

}
