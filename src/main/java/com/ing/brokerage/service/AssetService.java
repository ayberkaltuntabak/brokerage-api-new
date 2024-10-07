package com.ing.brokerage.service;

import com.ing.brokerage.dto.AssetDTO;
import com.ing.brokerage.entity.Asset;
import com.ing.brokerage.enums.Currency;
import com.ing.brokerage.enums.SizeOperation;
import com.ing.brokerage.repository.AssetRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

/**
 * @author ayberk.altuntabak
 */
@Service
public class AssetService {

  private final AssetRepository assetRepository;

  public AssetService(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
  }

  /**
   * Find all assets for a specific customer
   *
   * @param customerId - ID of the customer
   * @return List of assets owned by the customer
   */
  public List<AssetDTO> getAssetsByCustomerId(Long customerId) {
    return assetRepository.findByCustomerId(customerId).stream().map(this::getAssetDto).toList();
  }

  public void create(Long customerId, String assetName, Double size, Double usableSize){
    Asset asset = new Asset();
    asset.setSize(size);
    asset.setUsableSize(usableSize);
    asset.setCustomerId(customerId);
    asset.setAssetName(assetName);
    assetRepository.save(asset);
  }

  private AssetDTO getAssetDto(Asset asset) {
    AssetDTO assetDTO = new AssetDTO();
    assetDTO.setSize(asset.getSize());
    assetDTO.setCustomerId(asset.getCustomerId());
    assetDTO.setUsableSize(asset.getUsableSize());
    assetDTO.setAssetName(asset.getAssetName());
    return assetDTO;

  }

  /**
   * Find a specific asset for a customer by asset name.
   * Useful for checking balances (e.g., TRY balance).
   *
   * @param customerId - ID of the customer
   * @param assetName  - Name of the asset (e.g., "TRY")
   * @return The asset for the given customer and asset name
   */
  public Asset getAssetByCustomerIdAndAssetName(Long customerId, String assetName) {
    return assetRepository.findByCustomerIdAndAssetName(customerId, assetName);
  }

  /**
   * Deposit to a customer's asset balance.
   *
   * @param customerId - ID of the customer
   * @param amount     - Amount to deposit
   */
  @Transactional
  public void depositMoney(Long customerId, Double amount) {
    Asset asset = assetRepository.findByCustomerIdAndAssetName(customerId, Currency.TRY.name());
    if (isNull(asset)) {
      // If asset doesn't exist for the customer, create a new one
      asset = new Asset();
      asset.setCustomerId(customerId);
      asset.setAssetName(Currency.TRY.getName());
      asset.setSize(0.0);
      asset.setUsableSize(0.0);
    }
    // Increase the usable size for the asset
    asset.setUsableSize(asset.getUsableSize() + amount);
    asset.setSize(asset.getSize() + amount);
    assetRepository.save(asset);
  }

  /**
   * Withdraw from a customer's asset balance.
   * Checks if there are enough funds to withdraw.
   *
   * @param customerId - ID of the customer
   * @param amount     - Amount to withdraw
   * @throws IllegalArgumentException - If insufficient funds
   */
  @Transactional
  public void withdrawMoney(Long customerId, Double amount) {
    Asset asset = assetRepository.findByCustomerIdAndAssetName(customerId, Currency.TRY.name());
    if (isNull(asset)) {
      throw new IllegalArgumentException("Asset is not found for given customer");
    }
    if (asset.getUsableSize() < amount || asset.getSize() < amount) {
      throw new IllegalArgumentException("Insufficient funds for withdrawal.");
    }
    // Decrease the usable size for the asset
    asset.setUsableSize(asset.getUsableSize() - amount);
    asset.setSize(asset.getSize()  -amount);
    assetRepository.save(asset);
  }

  /**
   * Update the asset sizes (used during order placement or cancellation).
   *
   * @param customerId - ID of the customer
   * @param assetName  - Name of the asset
   * @param sizeChange - Change in size (positive for increasing, negative for decreasing)
   */
  @Transactional
  public void updateAssetSize(Long customerId, String assetName, Double sizeChange, SizeOperation sizeOperation) {
    Asset asset = assetRepository.findByCustomerIdAndAssetName(customerId, assetName);
    if (asset == null) {
      throw new IllegalArgumentException("Asset not found for the customer.");
    }
    // Update the usable size based on the sizeChange
    if (SizeOperation.INCREASE.equals(sizeOperation)) {
      asset.setUsableSize(asset.getUsableSize() + sizeChange);
    }else {
      asset.setUsableSize(asset.getUsableSize() - sizeChange);
    }
    assetRepository.save(asset);
  }
}