package org.example.currencyexchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CurrencyRate(@JsonProperty("rates") List<Rate> rates) {
}
