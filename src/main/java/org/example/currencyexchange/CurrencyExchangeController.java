package org.example.currencyexchange;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class CurrencyExchangeController {

    public static final String MESSAGE_IF_AMOUNT_EQUAL_OR_LESS_THAN_ZERO = "Amount must be greater than zero.";
    private final NbpCurrFetcher nbpCurrFetcher;

    @GetMapping("/currency-exchange")
    public ResponseEntity<ResponseDto> getCurrencyExchange(@RequestParam BigDecimal amount,
                                                           @RequestParam String sourceCurrency,
                                                           @RequestParam String targetCurrency) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body(new ResponseDto(MESSAGE_IF_AMOUNT_EQUAL_OR_LESS_THAN_ZERO));
        }

        try {
            String result = nbpCurrFetcher.currExchange(amount, sourceCurrency, targetCurrency);
            return ResponseEntity.ok(new ResponseDto(result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("Unexpected error occurred: " + e.getMessage()));
        }
    }

}
