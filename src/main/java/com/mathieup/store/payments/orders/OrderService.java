package com.mathieup.store.payments.orders;

import com.mathieup.store.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.getAllByCustomer(user);
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public OrderDto getOrder(Long orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        var user = authService.getCurrentUser();
        if (!order.belongsTo(user)) {throw new AccessDeniedException("Order doesn't belong to this user");}
        return orderMapper.toDto(order);
    }

}
