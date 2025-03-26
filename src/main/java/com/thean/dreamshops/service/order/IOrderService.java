package com.thean.dreamshops.service.order;

import com.thean.dreamshops.dto.OrderDTO;
import com.thean.dreamshops.model.Order;
import com.thean.dreamshops.model.OrderItem;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDTO getOrder(Long orderId);
    List<OrderDTO> getUserOrders(Long userId);

    OrderDTO convertToOrderDTO(Order order);

    Order createOrderFromOrderItem(OrderItem orderItem, Long userId);
}
