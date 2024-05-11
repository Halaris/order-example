package org.nordnet.orderexample.repositories;

import org.nordnet.orderexample.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
