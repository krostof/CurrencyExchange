package org.example.currencyexchange;

import lombok.RequiredArgsConstructor;
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

    @GetMapping("/")
    public ResponseEntity<ResponseDto> currencyExchange(@RequestParam BigDecimal amount,
                                                        @RequestParam String targetCurrency,
                                                        @RequestParam String currency) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body(new ResponseDto(MESSAGE_IF_AMOUNT_EQUAL_OR_LESS_THAN_ZERO));
        }

        try {
            String result = nbpCurrFetcher.currExchange(amount, currency, targetCurrency);
            ResponseDto responseDto = new ResponseDto(result);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
        }
    }
}
