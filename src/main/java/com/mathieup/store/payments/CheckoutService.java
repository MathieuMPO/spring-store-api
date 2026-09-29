package com.mathieup.store.payments;

import com.mathieup.store.payments.orders.Order;
import com.mathieup.store.payments.orders.carts.CartNotFoundException;
import com.mathieup.store.payments.orders.carts.EmptyCartException;
import com.mathieup.store.payments.orders.carts.CartRepository;
import com.mathieup.store.payments.orders.OrderRepository;
import com.mathieup.store.auth.AuthService;
import com.mathieup.store.payments.orders.carts.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CheckoutService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public CheckoutResponse checkout(UUID cartId) throws PaymentException {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(CartNotFoundException::new);
        if (cart.isEmpty()) {throw new EmptyCartException();}

        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);

        // Create a checkout session
        try {

            var session = paymentGateway.createCheckoutSession(order);

            cartService.clearCart(cartId);

            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());

        } catch (PaymentException ex) {
            orderRepository.delete(order);
            throw ex;
        }
    }

    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
            var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
            order.setStatus(paymentResult.getPaymentStatus());
            orderRepository.save(order);
        });
    }

}
