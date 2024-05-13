package org.nordnet.orderexample.repositories;

import org.nordnet.orderexample.model.CurrencyConversion;
import org.nordnet.orderexample.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyConversionRepository extends JpaRepository<CurrencyConversion, String> {
}
