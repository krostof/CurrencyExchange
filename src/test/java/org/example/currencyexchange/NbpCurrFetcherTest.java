package org.example.currencyexchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class NbpCurrFetcherTest {

    @InjectMocks
    private NbpCurrFetcher nbpCurrFetcher;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCurrExchange_whenCurrencyRatesAreAvailable() {
        BigDecimal amount = new BigDecimal("100");
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";

        Rate usdRate = new Rate(new BigDecimal("4.00"), "USD");
        Rate eurRate = new Rate(new BigDecimal("4.50"), "EUR");
        CurrencyRate currencyRate = new CurrencyRate(Arrays.asList(usdRate, eurRate));

        ResponseEntity<CurrencyRate[]> responseEntity = new ResponseEntity<>(new CurrencyRate[]{currencyRate}, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(NbpCurrFetcher.URL), eq(CurrencyRate[].class))).thenReturn(responseEntity);

        String result = nbpCurrFetcher.currExchange(amount, sourceCurrency, targetCurrency);

        BigDecimal expectedValue = amount.multiply(eurRate.mid()).divide(usdRate.mid(), BigDecimal.ROUND_HALF_UP);
        assertEquals(expectedValue.toString(), result);
    }

    @Test
    void testCurrExchange_whenCurrencyIsPLN() {
        BigDecimal amount = new BigDecimal("200");
        String sourceCurrency = "PLN";
        String targetCurrency = "EUR";

        Rate eurRate = new Rate(new BigDecimal("4.50"), "EUR");
        CurrencyRate currencyRate = new CurrencyRate(List.of(eurRate));

        ResponseEntity<CurrencyRate[]> responseEntity = new ResponseEntity<>(new CurrencyRate[]{currencyRate}, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(NbpCurrFetcher.URL), eq(CurrencyRate[].class))).thenReturn(responseEntity);

        String result = nbpCurrFetcher.currExchange(amount, sourceCurrency, targetCurrency);

        BigDecimal expectedValue = amount.multiply(eurRate.mid()).divide(BigDecimal.ONE, BigDecimal.ROUND_HALF_UP);
        assertEquals(expectedValue.toString(), result);
    }

    @Test
    void testCurrExchange_whenCurrencyNotFound() {

        BigDecimal amount = new BigDecimal("100");
        String sourceCurrency = "USD";
        String targetCurrency = "XYZ";

        Rate usdRate = new Rate(new BigDecimal("4.00"), "USD");
        CurrencyRate currencyRate = new CurrencyRate(List.of(usdRate));

        ResponseEntity<CurrencyRate[]> responseEntity = new ResponseEntity<>(new CurrencyRate[]{currencyRate}, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(NbpCurrFetcher.URL), eq(CurrencyRate[].class))).thenReturn(responseEntity);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> nbpCurrFetcher.currExchange(amount, sourceCurrency, targetCurrency),
                "Expected currExchange() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Currency rate not found"),
                "Exception message should contain 'Currency rate not found'");
    }
}
