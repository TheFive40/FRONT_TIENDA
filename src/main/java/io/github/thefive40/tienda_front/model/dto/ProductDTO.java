package io.github.thefive40.tienda_front.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
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

    private boolean status;

    private List<ReviewDTO> reviews = new ArrayList<> ( );

    private List<ProductCategoryDTO> productsCategory = new ArrayList<> ( );

    private List<DetailOrderDTO> detailsOrder = new ArrayList<> ( );

    private List<ItemCartDTO> itemsCart = new ArrayList<> ( );

    private List<DetailInvoiceDTO> invoices = new ArrayList<> (  );

    public ProductDTO(){
        dateRegistration = new Date (  );
        status = true;
    }
}
