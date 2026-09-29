package com.mathieup.store.payments.orders.carts.products;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Byte categoryId;
}
