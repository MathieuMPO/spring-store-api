package com.mathieup.store.payments.orders;

import com.mathieup.store.common.ErrorDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(
            @PathVariable("orderId") Long orderId
    ){
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> handleOrderNotFound(OrderNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleForbiddenAccess(AccessDeniedException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(ex.getMessage()));
    }





}
