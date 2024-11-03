package io.github.thefive40.tienda_front.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewDTO {
    private Long idReview;
    private ProductDTO product;
    private ClientDTO idClient;

}
