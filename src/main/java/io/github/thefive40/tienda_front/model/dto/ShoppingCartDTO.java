package io.github.thefive40.tienda_front.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@ToString
public class ShoppingCartDTO {
    private Long idCart;
    private ClientDTO client;
    private List<ItemCartDTO> itemsCart = new ArrayList<> ();
    private Date startDate;

    public ShoppingCartDTO () {
        startDate = new Date (  );
    }
}
