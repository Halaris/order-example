package org.nordnet.orderexample.controllers;

import lombok.extern.java.Log;
import org.nordnet.orderexample.model.CurrencyConversion;
import org.nordnet.orderexample.model.Orders;
import org.nordnet.orderexample.model.OrdersDto;
import org.nordnet.orderexample.model.Summary;
import org.nordnet.orderexample.repositories.CurrencyConversionRepository;
import org.nordnet.orderexample.repositories.OrderRepository;
import org.nordnet.orderexample.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/order")
@Log
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CurrencyConversionRepository currencyConversionRepository;

    @GetMapping(value= "/{id}")
    public OrdersDto GetOrder(@PathVariable Long id) {
        return orderService.GetOrder(id);
    }

    @PostMapping
    public OrdersDto AddOrder(@RequestBody OrdersDto order) {
        return orderService.AddOrder(order);
    }

    @GetMapping(value= "/ticker/{ticker}/action/{action}/currency/{currency}")
    public Summary GetSummary(@PathVariable String ticker, @PathVariable String action, @PathVariable String currency) {
        return orderService.GetSummary(ticker, action, currency);
    }
}
