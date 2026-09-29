package com.mathieup.store.payments.orders.carts;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be greater or equal than 1")
    @Max(value = 100, message = "Quantity must be lower or equal than 100")
    private Integer quantity;
}
