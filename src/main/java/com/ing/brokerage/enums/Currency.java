package com.ing.brokerage.enums;

/**
 * @author ayberk.altuntabak
 */
public enum Currency {
  TRY("TRY");

  public String getName() {
    return name;
  }

  private final String name;

  Currency(String name) {
    this.name = name;
  }
}
