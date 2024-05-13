package org.nordnet.orderexample.repositories;

import lombok.extern.java.Log;
import org.nordnet.orderexample.model.CurrencyCoverterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.util.List;
import java.util.Map;
@Log
@Component
public class CurrencyConverterRestClient {


    private final RestClient restClient;

    private static final String SOURCE_CURRENCY_CODE = "EUR";
    private static final String SOURCE_KEY = "source";
    private static final String CURRENCIES_KEY = "currencies";
    private static final String BASE_URL = "http://api.currencylayer.com";
    private static final String ACCESS_KEY = "access_key";
    @Value("${currency_converter.rest_client.secret_key}")
    private String SECRET_KEY;

    private UriBuilder baseParameters(UriBuilder uriBuilder) {
        return uriBuilder.queryParam(ACCESS_KEY, SECRET_KEY);
    }

    public CurrencyConverterRestClient() {
        restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    @Cacheable(CURRENCIES_KEY)
    public CurrencyCoverterResponse getConversionRate(List<String> currencies) {
        log.info("Retrieving conversion rate for currencies " + currencies);
        return restClient.get()
                .uri(uriBuilder -> this.baseParameters(uriBuilder)
                        .path("/live")
                        .queryParam(SOURCE_KEY, SOURCE_CURRENCY_CODE)
                        .queryParam(CURRENCIES_KEY, String.join(",", currencies))
                        .build())
                .retrieve()
                .body(CurrencyCoverterResponse.class);
    }



}
