package com.example.kafka_camel_spring_project.Dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonateDto {

    @JsonProperty("Nickname")
    private String Nickname;

    @JsonProperty("Sum")
    private String Sum;
}
