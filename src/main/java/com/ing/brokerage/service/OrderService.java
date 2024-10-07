package com.ing.brokerage.service;

import com.ing.brokerage.dto.OrderCancelRequestDTO;
import com.ing.brokerage.dto.OrderCreateRequestDTO;
import com.ing.brokerage.dto.OrderDTO;
import com.ing.brokerage.entity.Asset;
import com.ing.brokerage.entity.Order;
import com.ing.brokerage.enums.Currency;
import com.ing.brokerage.enums.OrderSide;
import com.ing.brokerage.enums.OrderStatus;
import com.ing.brokerage.enums.SizeOperation;
import com.ing.brokerage.repository.OrderRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

/**
 * @author ayberk.altuntabak
 */
@Service
public class OrderService {

  private final OrderRepository orderRepository;

  private final AssetService assetService;

  public OrderService(OrderRepository orderRepository, AssetService assetService) {
    this.orderRepository = orderRepository;
    this.assetService = assetService;
  }

  @Transactional
  public OrderDTO createOrder(OrderCreateRequestDTO orderCreateRequestDTO) {
    Asset customerTryAsset = assetService.getAssetByCustomerIdAndAssetName(orderCreateRequestDTO.getCustomerId(),
                                                                           Currency.TRY.getName());
    if (isNull(customerTryAsset)) {
      throw new IllegalArgumentException("Customer's TRY asset is absent");
    }
    Double costOfOrder = orderCreateRequestDTO.getPrice() * orderCreateRequestDTO.getSize();
    Asset existingAsset =
        assetService.getAssetByCustomerIdAndAssetName(orderCreateRequestDTO.getCustomerId(),
                                                      orderCreateRequestDTO.getAssetName());
    if (OrderSide.BUY.equals(orderCreateRequestDTO.getOrderSide())) {
      if (customerTryAsset.getUsableSize() < costOfOrder) {
        throw new IllegalArgumentException("Customer's TRY asset is not enough to buy asset");
      }

      if (!isNull(existingAsset)) {
        assetService.updateAssetSize(orderCreateRequestDTO.getCustomerId(),
                                     orderCreateRequestDTO.getAssetName(),
                                     orderCreateRequestDTO.getSize(),
                                     SizeOperation.INCREASE);
      }
      else {
        assetService.create(orderCreateRequestDTO.getCustomerId(), orderCreateRequestDTO.getAssetName(),
                            orderCreateRequestDTO.getSize(), orderCreateRequestDTO.getSize());
      }
      assetService.withdrawMoney(orderCreateRequestDTO.getCustomerId(), costOfOrder);
    }
    if (OrderSide.SELL.equals(orderCreateRequestDTO.getOrderSide())) {
      if (isNull(existingAsset)) {
        throw new IllegalArgumentException("Customer doesnt have given asset");
      }
      if (existingAsset.getUsableSize() < orderCreateRequestDTO.getSize()) {
        throw new IllegalArgumentException("Customer's TRY asset is not enough to buy asset");
      }
      assetService.updateAssetSize(orderCreateRequestDTO.getCustomerId(),
                                   orderCreateRequestDTO.getAssetName(),
                                   orderCreateRequestDTO.getSize(),
                                   SizeOperation.DECREASE);
      assetService.depositMoney(orderCreateRequestDTO.getCustomerId(), costOfOrder);
    }
    Order order = new Order();
    order.setOrderSide(orderCreateRequestDTO.getOrderSide());
    order.setCreateDate(LocalDateTime.now());
    order.setAssetName(orderCreateRequestDTO.getAssetName());
    order.setPrice(orderCreateRequestDTO.getPrice());
    order.setStatus(OrderStatus.PENDING);
    order.setSize(orderCreateRequestDTO.getSize());
    order.setCustomerId(orderCreateRequestDTO.getCustomerId());
    orderRepository.save(order);
    return getOrderDTO(order);
  }

  @Transactional
  public void cancelOrder(OrderCancelRequestDTO orderCancelRequestDTO) {
    Order order = orderRepository.findById(
                                     orderCancelRequestDTO.getOrderId())
                                 .orElseThrow(() -> new IllegalArgumentException("Order doesn't exist for customer"));
    Asset customerTryAsset = assetService.getAssetByCustomerIdAndAssetName(order.getCustomerId(),
                                                                           Currency.TRY.getName());
    if (isNull(customerTryAsset)) {
      throw new IllegalArgumentException("Customer's TRY asset is absent");
    }
    Double costOfOrder = order.getPrice() * order.getSize();
    if (!OrderStatus.PENDING.equals(order.getStatus())) {
      throw new IllegalArgumentException("Status should be pending");
    }
    if (OrderSide.BUY.equals(order.getOrderSide())) {
      assetService.depositMoney(order.getCustomerId(), costOfOrder);
    }
    if (OrderSide.SELL.equals(order.getOrderSide())) {
      assetService.withdrawMoney(order.getCustomerId(), costOfOrder);
    }
    order.setStatus(OrderStatus.CANCELLED);
    orderRepository.save(order);
  }

  public List<OrderDTO> getOrdersForCustomers(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
    List<Order> byCustomerIdAndCreateDateBetween =
        orderRepository.findByCustomerIdAndCreateDateBetween(customerId, startDate, endDate);
    return byCustomerIdAndCreateDateBetween.stream().map(this::getOrderDTO).toList();
  }

  private OrderDTO getOrderDTO(Order order) {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setOrderSide(order.getOrderSide());
    orderDTO.setCustomerId(order.getCustomerId());
    orderDTO.setPrice(order.getPrice());
    orderDTO.setStatus(order.getStatus());
    orderDTO.setAssetName(order.getAssetName());
    orderDTO.setId(order.getId());
    orderDTO.setSize(orderDTO.getSize());
    orderDTO.setCreateDate(order.getCreateDate());
    return orderDTO;
  }
}
