package com.example.kafka.consumerretry;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsumerRetryApplicationTests {

	@ClassRule
	public static EmbeddedKafkaRule embedded = new EmbeddedKafkaRule(1);

	@Autowired
	private KafkaTemplate<String, String> template;

	@Test
	public void contextLoads() throws InterruptedException {
		this.template.send("testTopic", "MyTestMessage");
		Thread.sleep(30_000);
	}

}
