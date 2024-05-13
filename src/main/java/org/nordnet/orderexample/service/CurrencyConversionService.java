package org.nordnet.orderexample.service;

import lombok.extern.java.Log;
import org.nordnet.orderexample.model.CurrencyConversion;
import org.nordnet.orderexample.model.CurrencyCoverterResponse;
import org.nordnet.orderexample.repositories.CurrencyConversionRepository;
import org.nordnet.orderexample.repositories.CurrencyConverterRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log
public class CurrencyConversionService {
    @Autowired
    CurrencyConverterRestClient currencyConverterRestClient;
    @Autowired
    CurrencyConversionRepository conversionRepository;

    @Scheduled(fixedDelay = 60000)
    public void updateCurrencyConversions() {
        List<CurrencyConversion> conversions = conversionRepository.findAll();
        if(conversions.isEmpty()) return;
        List<String> currencies = conversions.stream().map(c-> c.getCurrency()).collect(Collectors.toList());
        CurrencyCoverterResponse response = currencyConverterRestClient.getConversionRate(currencies);
        if(response.getQuotes().isEmpty()) return;
        List<CurrencyConversion> conversionsUpdated = response.getQuotes()
                .entrySet().stream()
                .map(entry ->
                     new CurrencyConversion(entry.getKey().substring(3), BigDecimal.ONE.divide(entry.getValue(),5, RoundingMode.HALF_UP))
                ).collect(Collectors.toList());
        log.info("conversions updated: " + conversionsUpdated);
        conversionRepository.saveAll(conversionsUpdated);
    }
}
