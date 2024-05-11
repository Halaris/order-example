package org.nordnet.orderexample.controllers;

import org.nordnet.orderexample.model.Order;
import org.nordnet.orderexample.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    @GetMapping
    public Order GetOrder(Long id) {
        return orderRepository.getReferenceById(id);
    }

    @PostMapping
    public Order AddOrder(@RequestBody Order order) {
        return orderRepository.save(order);
    }
}
