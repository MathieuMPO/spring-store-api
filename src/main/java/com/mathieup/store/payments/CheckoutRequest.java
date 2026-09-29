package com.mathieup.store.payments;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {

    @NotNull(message = "cartId must be provided")
    private UUID cartId;
}
