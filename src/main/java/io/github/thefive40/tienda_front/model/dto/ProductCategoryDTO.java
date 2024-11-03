package io.github.thefive40.tienda_front.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryDTO {
    private Long idProductCategory;
    private ProductDTO product;
    private CategoryDTO category;

}
