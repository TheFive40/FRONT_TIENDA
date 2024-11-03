package io.github.thefive40.tienda_front.model.dto;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InvoiceDTO {
    private Long idInvoice;

    private int nroInvoice;

    private OrderDTO order;

    private Date startDate;

    private double subTotal;

    private double tax;

    private double discount;

    private double total;
}
