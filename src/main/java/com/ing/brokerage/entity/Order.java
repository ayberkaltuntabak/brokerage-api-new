package com.ing.brokerage.entity;

import com.ing.brokerage.enums.OrderSide;
import com.ing.brokerage.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author ayberk.altuntabak
 */
@Entity
@Table(name = "stock_order")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long customerId;  // Just a customer identifier

  private String assetName;

  @Enumerated(EnumType.STRING)
  private OrderSide orderSide;

  private Double size;

  private Double price;
  @Enumerated(EnumType.STRING)
  private OrderStatus status; // PENDING, MATCHED, CANCELED

  private LocalDateTime createDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public String getAssetName() {
    return assetName;
  }

  public void setAssetName(String assetName) {
    this.assetName = assetName;
  }

  public OrderSide getOrderSide() {
    return orderSide;
  }

  public void setOrderSide(OrderSide orderSide) {
    this.orderSide = orderSide;
  }

  public Double getSize() {
    return size;
  }

  public void setSize(Double size) {
    this.size = size;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }
}
