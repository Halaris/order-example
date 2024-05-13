package org.nordnet.orderexample.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@ToString
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue
    Long id;
    @Column(length=50, nullable=false)
    String ticker;
    @Column(length=50, nullable=false)
    String action;
    @Column()
    Integer volume;
    @Column()
    BigDecimal price;
    @ManyToOne
    @JoinColumn(name="currency", nullable=false)
    CurrencyConversion currencyConversion;
}
