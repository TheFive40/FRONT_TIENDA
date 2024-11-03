package io.github.thefive40.tienda_front.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO {
    private Long productId;

    private String name;

    private String description;

    private double price;

    private String img;

    private Date dateRegistration;

    private ClientDTO client;

    private List<ReviewDTO> reviews = new ArrayList<> ( );

    private List<ProductCategoryDTO> productsCategory = new ArrayList<> ( );

    private List<DetailOrderDTO> detailsOrder = new ArrayList<> ( );

    private List<ItemCartDTO> itemsCart = new ArrayList<> ( );

}
