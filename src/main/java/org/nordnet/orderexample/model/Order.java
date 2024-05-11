package org.nordnet.orderexample.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue
    Long id;
    @Column(length=50, nullable=false)
    String ticker;
    @Column(length=50, nullable=false)
    String action;

    Integer volume;
    BigDecimal price;
    String currency;
}
