package io.github.thefive40.tienda_front.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO {
    private Long idOrder;
    private double total;
    private Date orderDate;
    private String address;
    private String city;
    private String zipCode;
    private String country;
    private String paymentMethod;
    private String discountCode;
    private ClientDTO idClient;
    private List<DetailOrderDTO> detailOrder = new ArrayList<> ( );
    private List<InvoiceDTO> invoices = new ArrayList<> (  );

    @Override
    public boolean equals ( Object object ) {
        if (this == object) return true;
        if (!(object instanceof OrderDTO orderDTO)) return false;
        return Objects.equals ( getIdOrder ( ), orderDTO.getIdOrder ( ) );
    }

    @Override
    public int hashCode () {
        return Objects.hashCode ( getIdOrder ( ) );
    }
}
