package io.github.thefive40.tienda_front.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCartDTO {
    private Long idCart;
    private ShoppingCartDTO shoppingCart;
    private ProductDTO product;

}
