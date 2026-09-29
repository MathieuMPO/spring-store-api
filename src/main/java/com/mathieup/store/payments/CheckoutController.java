package com.mathieup.store.payments;

import com.mathieup.store.common.ErrorDto;
import com.mathieup.store.payments.orders.carts.CartNotFoundException;
import com.mathieup.store.payments.orders.carts.EmptyCartException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@AllArgsConstructor
@Tag(name = "Checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;


    @PostMapping
    public CheckoutResponse checkout(
            @Valid @RequestBody CheckoutRequest request
    ) {
        return checkoutService.checkout(request.getCartId());
    }

    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestHeader() Map<String,String> headers,
            @RequestBody String payload
    ) {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }

    @ExceptionHandler({CartNotFoundException.class, EmptyCartException.class})
    public ResponseEntity<ErrorDto> handleCartNotFound(Exception ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler({PaymentException.class})
    public ResponseEntity<ErrorDto> handlePaymentException(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a checkout session"));
    }

}
