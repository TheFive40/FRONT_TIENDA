package io.github.thefive40.tienda_front.repository;

import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.model.dto.ProductDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository {
    ProductDTO getProductById ( long id );

    List<ProductDTO> getProductByClient ( ClientDTO client );

    List<ProductDTO> getProducts ();

    void save(ProductDTO productDTO);

    ProductDTO findProductByNameAndImgAndPrice ( String name, String url, String price );
}
