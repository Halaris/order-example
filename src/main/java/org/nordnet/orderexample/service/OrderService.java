package org.nordnet.orderexample.service;

import lombok.extern.java.Log;
import org.nordnet.orderexample.exceptions.OrderNotFoundException;
import org.nordnet.orderexample.mappers.OrderMapper;
import org.nordnet.orderexample.model.CurrencyConversion;
import org.nordnet.orderexample.model.Orders;
import org.nordnet.orderexample.model.OrdersDto;
import org.nordnet.orderexample.model.Summary;
import org.nordnet.orderexample.repositories.CurrencyConversionRepository;
import org.nordnet.orderexample.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
@Log
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CurrencyConversionRepository currencyConversionRepository;

    public OrdersDto GetOrder(@PathVariable Long id) {
        Optional<Orders> model = orderRepository.findById(id);
        if(model.isEmpty()) {
            throw new OrderNotFoundException(id);
        }
        return OrderMapper.toDto(model.get());
    }

    public OrdersDto AddOrder(OrdersDto order) {
        log.info("Adding order: " + order);
        String currency = order.getCurrency().toUpperCase();
        Optional<CurrencyConversion> currencyConversion = currencyConversionRepository.findById(currency);
        if(currencyConversion.isEmpty()) {
            currencyConversion = Optional.of(currencyConversionRepository.saveAndFlush(new CurrencyConversion(currency, BigDecimal.ONE)));
        }
        Orders toSave = OrderMapper.toModel(order,currencyConversion.get());
        toSave = orderRepository.save(toSave);
        return OrderMapper.toDto(toSave);
    }


    public Summary GetSummary(@PathVariable String ticker, @PathVariable String action, @PathVariable String currency) {
        Map<String, Object> res = orderRepository.findSummaryByTickerAndAction(ticker, action);
        BigDecimal conversionDivisor = BigDecimal.ONE;
        if(!currency.equalsIgnoreCase("EUR")) {
            Optional<CurrencyConversion> currencyConversionOptional =currencyConversionRepository.findById(currency);
            if (!currencyConversionOptional.isPresent()) {
                throw new RuntimeException("Currency not found");
            }
            conversionDivisor = currencyConversionOptional.get().getConversion();
        }

        Summary summary = Summary.builder()
                .action((String) res.get("action"))
                .ticker((String) res.get("ticker"))
                .min((BigDecimal) res.get("min"))
                .avg((BigDecimal) res.get("avg"))
                .max((BigDecimal) res.get("max"))
                .build();
        if(summary.getMax() == null || summary.getMin() == null || summary.getAvg() == null) return summary;
        summary.setMax(summary.getMax().divide(conversionDivisor));
        summary.setMin(summary.getMin().divide(conversionDivisor));
        summary.setAvg(summary.getAvg().divide(conversionDivisor));
        log.info("Summary: " + summary);
        return summary;
    }
}
