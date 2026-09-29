package com.mathieup.store.payments.orders;

import com.mathieup.store.payments.orders.carts.Cart;
import com.mathieup.store.auth.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@Table(name = "orders")
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private Set<OrderItem> items = new HashSet<>();

    public static Order fromCart (Cart cart, User user){
        var order = Order.builder()
                .customer(user)
                .status(PaymentStatus.PENDING)
                .totalPrice(cart.getTotalPrice())
                .build();

        cart.getItems().forEach(cartItem -> {
            var  orderItem = new OrderItem(cartItem, order);
            order.items.add(orderItem);
        });

        return order;
    }

    public Boolean belongsTo(User customer){
        return this.customer.equals(customer);
    }

}