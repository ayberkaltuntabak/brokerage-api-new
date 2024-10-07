package com.ing.brokerage.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * @author ayberk.altuntabak
 */
public class WithdrawRequestDTO {

  @NotNull
  private Long customerId;

  @NotNull
  @Positive(message = "Withdrawal amount must be a positive value.")
  private Double amount;


  // Getters and setters
  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

}