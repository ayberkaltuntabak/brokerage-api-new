package com.ing.brokerage.dto;

/**
 * @author ayberk.altuntabak
 */
public class AssetDTO {

  private Long customerId;
  private String assetName;
  private Double size;
  private Double usableSize;

  // Constructors
  public AssetDTO() {}

  // Getters and setters
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

  public Double getSize() {
    return size;
  }

  public void setSize(Double size) {
    this.size = size;
  }

  public Double getUsableSize() {
    return usableSize;
  }

  public void setUsableSize(Double usableSize) {
    this.usableSize = usableSize;
  }
}
