package com.ing.brokerage.controller;

import com.ing.brokerage.dto.AssetDTO;
import com.ing.brokerage.dto.DepositRequestDTO;
import com.ing.brokerage.dto.WithdrawRequestDTO;
import com.ing.brokerage.service.AssetService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author ayberk.altuntabak
 */
@RestController
@RequestMapping("/asset")
public class AssetController {

  @Autowired
  private AssetService assetService;

  /**
   * Endpoint for depositing money to a customer's account.
   * @param depositRequestDto - Request DTO containing deposit details.
   * @return ResponseEntity with no content if successful.
   */
  @PostMapping("/deposit")
  public ResponseEntity<Void> depositMoney(@Valid @RequestBody DepositRequestDTO depositRequestDto) {
    assetService.depositMoney(depositRequestDto.getCustomerId(), depositRequestDto.getAmount());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Endpoint for withdrawing money from a customer's account.
   * @param withdrawRequestDto - Request DTO containing withdrawal details.
   * @return ResponseEntity with no content if successful.
   */
  @PostMapping("/withdraw")
  public ResponseEntity<Void> withdrawMoney(@Valid @RequestBody WithdrawRequestDTO withdrawRequestDto) {
    assetService.withdrawMoney(
        withdrawRequestDto.getCustomerId(),
        withdrawRequestDto.getAmount());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/{customerId}")
  public ResponseEntity<List<AssetDTO>> getAssetsForCustomer(@PathVariable Long customerId){
    return new ResponseEntity<>(assetService.getAssetsByCustomerId(customerId), HttpStatus.OK);
  }
}