package com.ing.brokerage.repository;

import com.ing.brokerage.entity.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ayberk.altuntabak
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  // Find all orders for a specific customer within a date range
  List<Order> findByCustomerIdAndCreateDateBetween(Long customerId, LocalDateTime startDate, LocalDateTime endDate);

}
