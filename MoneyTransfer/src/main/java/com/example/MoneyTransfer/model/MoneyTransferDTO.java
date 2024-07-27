package com.example.MoneyTransfer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoneyTransferDTO {

    private String clientusername;

    private String clientrecipient;

    private BigDecimal amountofmoney;

    @JsonCreator
    public MoneyTransferDTO(@JsonProperty("clientusername") String clientusername,
                            @JsonProperty("clientrecipient") String clientrecipient,
                            @JsonProperty("amountofmoney") BigDecimal amountofmoney) {
        this.clientusername = clientusername;
        this.clientrecipient = clientrecipient;
        this.amountofmoney = amountofmoney;
    }
}
