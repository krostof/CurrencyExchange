package org.example.currencyexchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record Rate(@JsonProperty("mid") BigDecimal mid,
                   @JsonProperty("code") String code) {
}
