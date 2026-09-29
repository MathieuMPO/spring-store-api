package com.mathieup.store.payments.orders.carts;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotNull(message = "Product id shouldn't be null")
    private Long productId;
}
