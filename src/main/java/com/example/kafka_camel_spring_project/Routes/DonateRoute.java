package com.example.kafka_camel_spring_project.Routes;


import com.example.kafka_camel_spring_project.Dto.DonateDto;
import com.example.kafka_camel_spring_project.model.DonateModel;
import com.example.kafka_camel_spring_project.generated.Donate;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DonateRoute  extends RouteBuilder{

    final String path = "kafka:%s?brokers=localhost:9093";

    @Override
    public void configure() throws Exception {

        from("direct:donate")
                .log("send to direct:to_db")
                .to("direct:to_db")
                .log("send to direct:to_kafka")
                .to("direct:to_kafka")
                .setHeader("MessageType", simple("SUCCESS"))
                .log("send to direct:status")
                .to("direct:status");

        from("direct:to_db")
                .log("start process")
                .process(exchange -> {
                    Donate donate = exchange.getIn().getBody(Donate.class);
                    DonateModel dm = new DonateModel();
                    dm.setNickname(donate.getUser().getNickname());

                    dm.setSum(donate.getSum().intValueExact());

                    exchange.getMessage().setBody(dm,DonateModel.class);

                })
                .log("save to database")
                .to("jpa:com.example.kafka_camel_spring_project.model.DonateModel");


        from("direct:to_kafka")
                .process(exchange -> {
                    DonateModel dm =exchange.getIn().getBody(DonateModel.class);

                    DonateDto dto = new DonateDto(dm.getNickname(),dm.getSum().toString());

                    exchange.getMessage().setBody(dto,DonateDto.class);

                })
                .marshal().json(JsonLibrary.Jackson)
                .toF(path,"result");
    }
}
