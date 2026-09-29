package com.mathieup.store.payments.orders.carts;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException(){
        super("Cart is empty");
    }
}
