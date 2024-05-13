package org.nordnet.orderexample.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;


@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Summary {
    private String ticker;
    private String action;
    private BigDecimal min;
    private BigDecimal avg;
    private BigDecimal max;

    public Summary(String ticker, String action, int min, Double avg, int max) {
        this.ticker = ticker;
        this.action = action;
        this.min = BigDecimal.valueOf(min);
        this.avg = BigDecimal.valueOf(avg);
        this.max = BigDecimal.valueOf(max);
    }
}
