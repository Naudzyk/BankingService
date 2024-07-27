package com.example.MoneyTransfer.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Builder
@Data
@Table(name = "moneytransfer")
public class MoneyTransfer implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "clientusername")
    private String clientusername;

    @Column(name = "clientrecipient")
    private String clientrecipient;

    @Column(name = "amountofmoney")
    private BigDecimal amountofmoney;


    public MoneyTransfer() {

    }

    public MoneyTransfer(Integer id, String clientusername, String clientrecipient, BigDecimal amountofmoney) {
        this.id = id;
        this.clientusername = clientusername;
        this.clientrecipient = clientrecipient;
        this.amountofmoney = amountofmoney;
    }
}
