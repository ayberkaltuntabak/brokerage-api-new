package com.ing.brokerage.repository;

import com.ing.brokerage.entity.Asset;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ayberk.altuntabak
 */
@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

  // Find an asset by customerId and assetName (for example, to find TRY balance)
  Asset findByCustomerIdAndAssetName(Long customerId, String assetName);

  // Find all assets for a specific customer
  List<Asset> findByCustomerId(Long customerId);
}
