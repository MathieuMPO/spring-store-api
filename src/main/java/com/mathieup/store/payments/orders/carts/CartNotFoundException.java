package com.mathieup.store.payments.orders.carts;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException() {
        super("Cart not found");
    }
}
