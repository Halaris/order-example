package org.nordnet.orderexample.controllers;

import org.nordnet.orderexample.model.CurrencyCoverterResponse;
import org.nordnet.orderexample.repositories.CurrencyConverterRestClient;
import org.nordnet.orderexample.service.CurrencyConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    CurrencyConverterRestClient currencyConverterRestClient;
    @Autowired
    CurrencyConversionService currencyConversionService;
    @GetMapping
    public CurrencyCoverterResponse testGetCurrencies() {
        CurrencyCoverterResponse response = currencyConverterRestClient.getConversionRate(List.of("USD", "SEK","NOK"));
        return response;
    }
    @PostMapping
    public void updateCurrencies() {
        currencyConversionService.updateCurrencyConversions();
    }
}
