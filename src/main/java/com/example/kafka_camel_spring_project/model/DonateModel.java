package com.example.kafka_camel_spring_project.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "donates")
public class DonateModel {

    @Id
    @Column(nullable = false)
    private String Nickname;

    @Column(nullable = false)
    private Integer Sum;



}
