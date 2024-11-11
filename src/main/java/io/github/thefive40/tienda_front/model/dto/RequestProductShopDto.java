package io.github.thefive40.tienda_front.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestProductShopDto {
    private ProductDTO product;
    private ShoppingCartDTO shoppingCart;
}
