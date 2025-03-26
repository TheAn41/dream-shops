package com.thean.dreamshops.controller;


import com.thean.dreamshops.dto.OrderDTO;
import com.thean.dreamshops.exception.NotFoundException;
import com.thean.dreamshops.model.Order;
import com.thean.dreamshops.model.OrderItem;
import com.thean.dreamshops.response.ApiResponse;
import com.thean.dreamshops.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/create-from-item")
    public ResponseEntity<OrderDTO> createOrderFromOrderItem(@RequestBody OrderItem orderItem, @RequestParam Long userId) {
        Order order = orderService.createOrderFromOrderItem(orderItem, userId);
        return ResponseEntity.ok(orderService.convertToOrderDTO(order));
    }

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            Order order =  orderService.placeOrder(userId);
            OrderDTO orderDTO = orderService.convertToOrderDTO(order);
            return ResponseEntity.ok(new ApiResponse("Item Order Success!", orderDTO));
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error Occured!", e.getMessage()));
        }
    }

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDTO orderDTO = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Item Order Success!", orderDTO));
        } catch (NotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/order")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            List<OrderDTO> orderDTOS = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Item Order Success!", orderDTOS));
        } catch (NotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!", e.getMessage()));
        }
    }
}
