package org.nordnet.orderexample.repositories;

import org.nordnet.orderexample.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query(value = "select  " +
            "o.ticker as \"ticker\"," +
            " o.action as \"action\"," +
            " MIN(o.price*cc.conversion) as \"min\"," +
            " AVG(o.price*cc.conversion) as \"avg\"," +
            " MAX(o.price*cc.conversion) as \"max\"" +
            "from orders as o " +
            "inner join currency_conversion as cc on o.currency = cc.currency " +
            "where o.ticker = ?1 and o.action = ?2 group by o.ticker, o.action",
            nativeQuery = true)
    public Map<String, Object> findSummaryByTickerAndAction(String ticker, String action);
}
