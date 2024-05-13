package org.nordnet.orderexample.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class CurrencyCoverterResponse {
    private boolean success;
    private Date timestamp;
    private String source;
    private Map<String, BigDecimal> quotes;
}
