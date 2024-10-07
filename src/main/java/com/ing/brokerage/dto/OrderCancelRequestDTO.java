package com.ing.brokerage.dto;

import jakarta.validation.constraints.NotNull;

/**
 * @author ayberk.altuntabak
 */
public class OrderCancelRequestDTO {

  @NotNull
  private Long orderId;

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }
}
