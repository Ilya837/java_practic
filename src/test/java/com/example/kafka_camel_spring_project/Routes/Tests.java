package com.example.kafka_camel_spring_project.Routes;

import com.example.kafka_camel_spring_project.KafkaCamelSpringProjectApplication;
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

	@EndpointInject("mock:kafka:result")
	 MockEndpoint kafkaResults;

	@EndpointInject("mock:kafka:status")
	 MockEndpoint kafkaStatus;

	@Test
	void StartTest(){}


	@Test
	void SaveToDB() throws InterruptedException {

		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Donate xmlns=\"/jaxb/gen\"><user><id>1</id><nickname>qwe1</nickname><email>qwe@mail.ru</email></user><Sum>1234</Sum></Donate>";

		DonateModel dm = new DonateModel();

		dm.setSum(1234);
		dm.setNickname("qwe1");

		saveToDb.expectedBodiesReceived(dm);

		producerTemplate.sendBody("direct:requests",data);

		saveToDb.assertIsSatisfied(5000);
	}

	@Test
	void SaveToKafkaResult() throws InterruptedException {
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Donate xmlns=\"/jaxb/gen\"><user><id>1</id><nickname>qwe2</nickname><email>qwe@mail.ru</email></user><Sum>1234</Sum></Donate>";
		kafkaResults.expectedBodiesReceived("{\"Nickname\":\"qwe2\",\"Sum\":\"1234\"}");

		producerTemplate.sendBody("direct:requests",data);

		MockEndpoint.assertIsSatisfied(kafkaResults);

	}

	@Test
	void SaveToKafkaStatus1() throws InterruptedException {
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Donate xmlns=\"/jaxb/gen\"><user><id>1</id><nickname>qwe3</nickname><email>qwe@mail.ru</email></user><Sum>1234</Sum></Donate>";
		kafkaStatus.expectedBodiesReceived("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
				"<Status xmlns=\"/jaxb/gen\">\n" +
				"    <statusType>SUCCESS</statusType>\n" +
				"    <message>Message saved in database and kafka</message>\n" +
				"</Status>\n");

		producerTemplate.sendBody("direct:requests", data);

		MockEndpoint.assertIsSatisfied(kafkaStatus);
	}

	@Test
	void SaveToKafkaStatus2() throws InterruptedException {
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Donate xmlns=\"/jaxb/gen\"><user><nickname>qwe4</nickname><email>qwe@mail.ru</email></user><Sum>1234</Sum></Donate>";
		kafkaStatus.expectedBodiesReceived("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
				"<Status xmlns=\"/jaxb/gen\">\n" +
				"    <message>Message not contain user.id</message>\n" +
				"</Status>\n");

		producerTemplate.sendBody("direct:requests", data);

		MockEndpoint.assertIsSatisfied(kafkaStatus);
	}

	@Test
	void SaveToKafkaStatus3 () throws InterruptedException {
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Donate xmlns=\"/jaxb/gen\"><user><id>1</id><email>qwe@mail.ru</email></user><Sum>1234</Sum></Donate>";
		kafkaStatus.expectedBodiesReceived("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
				"<Status xmlns=\"/jaxb/gen\">\n" +
				"    <message>Message not contain user.nickname</message>\n" +
				"</Status>\n");

		producerTemplate.sendBody("direct:requests", data);

		MockEndpoint.assertIsSatisfied(kafkaStatus);
	}

	@Test
	void SaveToKafkaStatus4() throws InterruptedException {
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Donate xmlns=\"/jaxb/gen\"><user><id>1</id><nickname>qwe3</nickname></user><Sum>1234</Sum></Donate>";
		kafkaStatus.expectedBodiesReceived("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
				"<Status xmlns=\"/jaxb/gen\">\n" +
				"    <message>Message not contain user.email</message>\n" +
				"</Status>\n");

		producerTemplate.sendBody("direct:requests", data);

		MockEndpoint.assertIsSatisfied(kafkaStatus);
	}

	@Test
	void SaveToKafkaStatus5() throws InterruptedException {
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Donate xmlns=\"/jaxb/gen\"><user><id>1</id><nickname>qwe3</nickname><email>qwe@mail.ru</email></user></Donate>";
		kafkaStatus.expectedBodiesReceived("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
				"<Status xmlns=\"/jaxb/gen\">\n" +
				"    <message>Message not contain Sum</message>\n" +
				"</Status>\n");

		producerTemplate.sendBody("direct:requests", data);

		MockEndpoint.assertIsSatisfied(kafkaStatus);
	}







}
