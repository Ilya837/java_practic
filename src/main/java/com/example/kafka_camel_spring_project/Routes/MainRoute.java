package com.example.kafka_camel_spring_project.Routes;

import com.example.kafka_camel_spring_project.generated.Donate;
import jakarta.xml.bind.UnmarshalException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class MainRoute extends RouteBuilder {

    @Value("${kafka.kafka1.camel-request-path}")
    private String path;

    @Override
    public void configure() throws Exception {


        try( DataFormat format = new JaxbDataFormat("com.example.kafka_camel_spring_project.generated")){

            onException(UnmarshalException.class)
                    .to("micrometer:counter:unmarshal")
                    .process(exchange -> {
                        exchange.getMessage().setBody("Unmarshal exception");
                        exchange.getIn().setHeader("Message type","ERROR");
                    })
                    .to("direct:status")
                    .log("unmarshalExpention");



                from(path)
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

                                    if(donate.getUser().getNickname() == null){
                                        exchange.getMessage().setBody("Message not contain user.nickname");
                                        exchange.getIn().setHeader("Message type","ERROR");
                                    }

                                    if(donate.getUser().getEmail() == null){
                                        exchange.getMessage().setBody("Message not contain user.email");
                                        exchange.getIn().setHeader("Message type","ERROR");
                                    }

                                    if(donate.getUser().getId() == null){
                                        exchange.getMessage().setBody("Message not contain user.id");
                                        exchange.getIn().setHeader("Message type","ERROR");
                                    }

                                }
                                else{
                                    System.out.println("User is null");
                                    exchange.getMessage().setBody("Message not contain name");

                                }

                                if(donate.getSum() != null){
                                    System.out.println("Sum is not null");
                                }
                                else{
                                    System.out.println("Sum is null");
                                    exchange.getMessage().setBody("Message not contain Sum");
                                    exchange.getIn().setHeader("Message type","ERROR");

                                }
                            })
                            .to("direct:choice")


                        .otherwise()
                            .setBody(simple("Message is not a Donate"))
                            .setHeader("MessageType", simple("ERROR"))
                            .log("send to direct:status")
                            .to("direct:status");


                from("direct:choice")
                        .choice()
                            .when(header("Message type").contains("ERROR"))
                                .log("send to direct:status")
                                .to("direct:status")
                            .otherwise()
                                .log("send to direct:donate")
                                .to("direct:donate");

        }
    }
}
