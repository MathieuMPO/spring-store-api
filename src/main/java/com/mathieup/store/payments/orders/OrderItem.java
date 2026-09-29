package com.mathieup.store.payments.orders;

import com.mathieup.store.payments.orders.carts.CartItem;
import com.mathieup.store.payments.orders.carts.products.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    public OrderItem(CartItem cartItem, Order order) {
        this.order = order;
        this.product = cartItem.getProduct();
        this.unitPrice = this.product.getPrice();
        this.quantity = cartItem.getQuantity();
        this.totalPrice = cartItem.getTotalPrice();
    }

}