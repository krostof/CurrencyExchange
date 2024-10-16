package org.example.currencyexchange;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Log4j2
@Service
public class NbpCurrFetcher {

    public static final String URL = "https://api.nbp.pl/api/exchangerates/tables/a/";
    RestTemplate restTemplate = new RestTemplate();

    @Cacheable("currencyRates")
    public String currExchange(BigDecimal amount, String currency, String targetCurrency) {
        ResponseEntity<CurrencyRate[]> response = restTemplate.getForEntity(URL, CurrencyRate[].class);
        CurrencyRate[] currencyRates = response.getBody();
        assert currencyRates != null;
        List<Rate> rates = currencyRates[0].rates();

        BigDecimal sourceRate = getCurrency(currency, rates);
        BigDecimal targetRate = getCurrency(targetCurrency, rates);

        BigDecimal result = getMultiply(amount, sourceRate, targetRate);

        log.info(result);

        return result.toString();
    }

    private static BigDecimal getMultiply(BigDecimal amount, BigDecimal sourceRate, BigDecimal targetRate) {
        return amount.multiply(targetRate).divide(sourceRate, BigDecimal.ROUND_HALF_UP);
    }

    private static BigDecimal getCurrency(String currency, List<Rate> rates) {
        if (currency.equalsIgnoreCase("PLN")) {
            return BigDecimal.ONE;
        }

        return rates.stream()
                .filter(rate -> rate.code().equalsIgnoreCase(currency))
                .map(Rate::mid)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Currency rate not found"));
    }
}
