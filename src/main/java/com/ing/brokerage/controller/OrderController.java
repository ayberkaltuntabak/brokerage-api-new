package com.ing.brokerage.controller;

import com.ing.brokerage.dto.OrderCancelRequestDTO;
import com.ing.brokerage.dto.OrderCreateRequestDTO;
import com.ing.brokerage.dto.OrderDTO;
import com.ing.brokerage.entity.Order;
import com.ing.brokerage.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author ayberk.altuntabak
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  /**
   * Endpoint for creating a new order.
   * @param requestDTO - Request DTO containing order details.
   * @return ResponseEntity containing the created order.
   */
  @PostMapping
  public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderCreateRequestDTO requestDTO) {
    OrderDTO createdOrder = orderService.createOrder(requestDTO);
    return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
  }

  /**
   * Endpoint for listing all orders for a customer within a date range.
   * @param customerId - ID of the customer.
   * @param startDate - Start date for filtering.
   * @param endDate - End date for filtering.
   * @return List of orders for the customer.
   */
  @GetMapping
  public ResponseEntity<List<OrderDTO>> listOrders(
      @RequestParam Long customerId,
      @RequestParam LocalDateTime startDate,
      @RequestParam LocalDateTime endDate) {
    List<OrderDTO> orders = orderService.getOrdersForCustomers(customerId, startDate, endDate);
    return new ResponseEntity<>(orders, HttpStatus.OK);
  }

  /**
   * Endpoint for canceling a pending order.
   * @param orderId - ID of the order to cancel.
   * @return ResponseEntity with no content if successful.
   */
  @DeleteMapping("/{orderId}")
  public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
    OrderCancelRequestDTO orderCancelRequestDTO = new OrderCancelRequestDTO();
    orderCancelRequestDTO.setOrderId(orderId);
    orderService.cancelOrder(orderCancelRequestDTO);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}