package com.mathieup.store.payments.orders.carts;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private ProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;

}
