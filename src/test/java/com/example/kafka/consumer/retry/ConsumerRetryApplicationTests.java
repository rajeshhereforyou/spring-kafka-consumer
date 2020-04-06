package com.example.kafka.consumer.retry;

import com.example.kafka.MyKafkaAbstractConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsumerRetryApplicationTests extends MyKafkaAbstractConfig {

	@Autowired
	private KafkaTemplate<String, String> template;

	@Test
	public void contextLoads() throws InterruptedException {
		this.template.send("testTopic", "MyTestMessage");
		Thread.sleep(30_000);
	}

}
