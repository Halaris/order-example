package org.nordnet.orderexample.model;

import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersDto {
    Long id;
    String ticker;
    String action;
    Integer volume;
    BigDecimal price;
    String currency;
}
