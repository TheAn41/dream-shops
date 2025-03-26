package com.thean.dreamshops.service.order;

import com.thean.dreamshops.dto.OrderDTO;
import com.thean.dreamshops.enums.OrderStatus;
import com.thean.dreamshops.exception.NotFoundException;
import com.thean.dreamshops.model.Cart;
import com.thean.dreamshops.model.Order;
import com.thean.dreamshops.model.OrderItem;
import com.thean.dreamshops.model.Product;
import com.thean.dreamshops.repository.OrderRepository;
import com.thean.dreamshops.repository.ProductRepository;
import com.thean.dreamshops.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrderForCart(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private Order createOrderForCart(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

private List<OrderItem> createOrderItems(Order order, Cart cart) {
    return cart.getItems().stream().map(cartItem -> {
        Product product = cartItem.getProduct();
        product.setInventory(product.getInventory() - cartItem.getQuantity());
        productRepository.save(product);
        return new OrderItem(order,
                product,
                cartItem.getQuantity(),
                cartItem.getUnitPrice());
    }).toList();
}



    public BigDecimal calculateTotalAmount (List<OrderItem> orderItemList){
        return orderItemList.stream()
                .map(item-> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDTO getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToOrderDTO)
                .orElseThrow(()->new NotFoundException("Order not found"));
    }

    @Override
    public List<OrderDTO> getUserOrders(Long userId) {

        return orderRepository.findByUserId(userId).stream().map(this::convertToOrderDTO).toList();
    }
@Override
public OrderDTO convertToOrderDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

    public Order createOrderFromOrderItem(OrderItem orderItem, Long userId) {
        // Lấy thông tin sản phẩm
        Product product = productRepository.findById(orderItem.getProduct().getId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        // Kiểm tra số lượng tồn kho
        if (product.getInventory() < orderItem.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setUser(orderItem.getOrder().getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());

        // Cập nhật tồn kho sản phẩm
        product.setInventory(product.getInventory() - orderItem.getQuantity());
        productRepository.save(product);

        // Gán orderItem vào order
        orderItem.setOrder(order);
        order.setOrderItems(new HashSet<>(List.of(orderItem)));

        // Tính tổng tiền
        order.setTotalAmount(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));

        // Lưu đơn hàng vào database
        return orderRepository.save(order);
    }
}
