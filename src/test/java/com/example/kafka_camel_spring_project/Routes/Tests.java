package com.example.kafka_camel_spring_project.Routes;

import com.example.kafka_camel_spring_project.model.DonateModel;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;


@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(properties = {"kafka.kafka1.camel-request-path=direct:requests"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints
class Tests {

	@Autowired
	private ProducerTemplate producerTemplate;


	@EndpointInject("mock:jpa:com.example.kafka_camel_spring_project.model.DonateModel")
	 MockEndpoint saveToDb;


	@Test
	void StartTest(){}


	@Test
	void SaveToDB() throws InterruptedException {

		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Donate xmlns=\"/jaxb/gen\"><user><id>1</id><nickname>qwe</nickname><email>qwe@mail.ru</email></user><Sum>1234</Sum></Donate>";

		DonateModel dm = new DonateModel();

		dm.setSum(1234);
		dm.setNickname("qwe");

		saveToDb.expectedBodiesReceived(dm);

		producerTemplate.sendBody("direct:requests",data);

		saveToDb.assertIsSatisfied(5000);
	}

}
