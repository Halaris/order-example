package org.nordnet.orderexample.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "currency_conversion")
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversion {
    @Id
    String currency;
    @Column()
    BigDecimal conversion;
}
