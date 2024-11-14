package io.github.thefive40.tienda_front.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCartDTO {
    private Long idCart;
    private ClientDTO client;
    private List<ItemCartDTO> itemsCart = new ArrayList<> ();
    private Date startDate;

    public ShoppingCartDTO () {
        startDate = new Date (  );
    }
}
