package com.ing.brokerage.dto;

import com.ing.brokerage.enums.OrderSide;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * @author ayberk.altuntabak
 */
public class OrderCreateRequestDTO {

  @NotNull
  private Long customerId;

  @NotNull
  private String assetName;

  @NotNull
  private OrderSide orderSide;

  @NotNull
  @Positive(message = "Size must be a positive value.")
  private Double size;

  @NotNull
  @Positive(message = "Price must be a positive value.")
  private Double price;

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
}
