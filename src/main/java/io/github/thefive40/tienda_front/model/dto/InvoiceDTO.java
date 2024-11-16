package io.github.thefive40.tienda_front.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InvoiceDTO {
    private Long idInvoice;

    private int nroInvoice;

    private Date startDate;

    private double subTotal;

    private double tax;

    private double discount;

    private double total;

    private ClientDTO client;

    private List<DetailInvoiceDTO> detailsInvoice = new ArrayList<> (  );
}
