package com.mathieup.store.payments.orders.carts;

import com.mathieup.store.payments.orders.carts.products.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Table(name = "carts")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created", insertable = false, updatable = false)
    private Date dateCreated;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart", orphanRemoval = true, cascade = CascadeType.MERGE)
    private Set<CartItem> items = new HashSet<>();

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem getItem(Long productId) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public CartItem addItem(Product product) {
        var cartItem = getItem(product.getId());
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(this);
            cartItem.setQuantity(1);
            items.add(cartItem);
        }
        return cartItem;
    }

    public void removeItem(Long productId) {
        var cartItem = getItem(productId);
        if (cartItem != null) {
            items.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public Boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }
}
