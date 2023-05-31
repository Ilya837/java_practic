package com.example.kafka_camel_spring_project.Routes;

import com.example.kafka_camel_spring_project.generated.Donate;
import jakarta.xml.bind.UnmarshalException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;


@Component
public class MainRoute extends RouteBuilder {

    final String path = "kafka:%s?brokers=localhost:9092";

    @Override
    public void configure() throws Exception {


        try( DataFormat format = new JaxbDataFormat("com.example.kafka_camel_spring_project.generated")){

            onException(UnmarshalException.class)
                    .to("micrometer:counter:unmarshal")
                    .log("unmarshalExpention");



                fromF(path,"main")
                    .log("Received data: ${body}")
                    .unmarshal(format)
                    .choice()
                        .when(body().isInstanceOf(Donate.class))
                            .process(exchange -> {

                                Donate donate = exchange.getIn().getBody(Donate.class);

                                System.out.println(exchange.getIn().getBody(String.class));

                                if(donate.getUser() != null){
                                    System.out.println("User is not null");
                                    exchange.getIn().setHeader("Messege type","SUCCESS");
                                }
                                else{
                                    System.out.println("User is null");
                                    exchange.getIn().setHeader("Message type","ERROR");

                                    exchange.getMessage().setBody("Message not contain name");

                                    if(donate.getSum() != null){
                                        System.out.println("Sum is not null");
                                    }
                                    else{
                                        System.out.println("Sum is null");
                                        exchange.getMessage().setBody("Message not contain name and sum");

                                    }
                                }

                                if(donate.getSum() != null){
                                    System.out.println("Sum is not null");
                                    exchange.getIn().setHeader("Message type","SUCCESS");
                                }
                                else{
                                    System.out.println("Sum is null");
                                    exchange.getMessage().setBody("Message not contain sum");
                                    exchange.getIn().setHeader("Message type","ERROR");

                                }
                            })
                            .choice()
                            .when(header("Message type").contains("ERROR"))
                                .setBody(simple("Message dosn't contain user or sum"))
                                .to("direct:status")
                            .otherwise()
                                .to("direct:donate")

                        .otherwise()
                            .setBody(simple("Message is not a Donate"))
                            .setHeader("MessageType", simple("ERROR"))
                            .to("direct:status");
        }
    }
}
