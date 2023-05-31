package com.example.kafka_camel_spring_project.Routes;

import com.example.kafka_camel_spring_project.generated.Status;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;


@Component
public class StatusRoute extends RouteBuilder {

    final String path = "kafka:%s?brokers=localhost:9092";



    @Override
    public void configure() throws Exception {

        try( DataFormat format = new JaxbDataFormat("com.example.kafka_camel_spring_project.generated")){


            from("direct:status")
                    .choice()
                    .when(header("Message type").contains("ERROR"))
                        .to("micrometer:counter:error")

                    .otherwise()
                        .to("micrometer:counter:sucsess")
                        .setBody(simple("Message saved in database and kafka"))
                    .end()
                    .process(exchange -> {
                        String message = exchange.getIn().getBody(String.class);

                        Status status = new Status();
                        status.setStatusType(exchange.getIn().getHeader("MessageType", String.class));
                        status.setMessage(message);

                        exchange.getMessage().setBody(status, Status.class);
                    })
                    .marshal(format)
                    .toF(path, "status");

        }
    }


}