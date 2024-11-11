package io.github.thefive40.tienda_front.repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.model.dto.ItemCartDTO;
import io.github.thefive40.tienda_front.model.dto.ShoppingCartDTO;

import java.util.List;

public interface ShoppingCartRepository {
    void save( ShoppingCartDTO shoppingCartDTO );
    ShoppingCartDTO findByClient( ClientDTO clientDTO ) throws JsonProcessingException;
    List<ShoppingCartDTO> findAll();
    void updateItemCart( ItemCartDTO itemCartDTO );
}
