package org.nordnet.orderexample.mappers;

import org.nordnet.orderexample.model.CurrencyConversion;
import org.nordnet.orderexample.model.Orders;
import org.nordnet.orderexample.model.OrdersDto;

public class OrderMapper {
    public static Orders toModel(OrdersDto orders, CurrencyConversion currencyConversion) {
        return Orders.builder()
                .ticker(orders.getTicker().toUpperCase())
                .action(orders.getAction().toUpperCase())
                .price(orders.getPrice())
                .volume(orders.getVolume())
                .currencyConversion(currencyConversion)
                .build();
    }

    public static OrdersDto toDto(Orders orders) {
        return OrdersDto.builder()
                .id(orders.getId())
                .ticker(orders.getTicker())
                .action(orders.getAction())
                .price(orders.getPrice())
                .volume(orders.getVolume())
                .currency(orders.getCurrencyConversion().getCurrency())
                .build();
    }
}
