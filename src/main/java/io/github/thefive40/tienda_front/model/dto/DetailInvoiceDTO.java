package io.github.thefive40.tienda_front.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DetailInvoiceDTO {
    private Long id;

    private InvoiceDTO invoice;

    private ProductDTO product;

    private int quantity;

    private double unitPrice;
}
