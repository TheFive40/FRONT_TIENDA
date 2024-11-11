package io.github.thefive40.tienda_front.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.model.dto.ProductDTO;
import io.github.thefive40.tienda_front.repository.ProductRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProductService implements ProductRepository {
    private HttpClient httpClient;


    private final ObjectMapper mapper;
    private final Logger logger;
    private final ClientDTO clientDTO;

    public ProductService ( @Qualifier("mapper") ObjectMapper mapper, Logger logger, ClientDTO clientDTO ) {
        this.mapper = mapper;
        this.logger = logger;
        this.clientDTO = clientDTO;
    }

    @Override
    public ProductDTO getProductById ( long id ) {
        ProductDTO productDTO = new ProductDTO ( );
        productDTO.setProductId ( id );
        try {
            return postRequest ( productDTO, null, "http://localhost:6060/api/products/findById" )
                    .get ( 0 );
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( e );
        }
    }

    @Override
    public List<ProductDTO> getProductByClient ( ClientDTO client ) {
        try {
            return postRequest ( null, clientDTO, "http://localhost:6060/api/products/findByClient" );
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( e );
        }

    }

    @Override
    public List<ProductDTO> getProducts () {
        return sendRequest ( "http://localhost:6060/api/products/findAll" );
    }

    @Override
    public void save ( ProductDTO productDTO ) {
        try {
            postRequest ( productDTO, null, "http://localhost:6060/api/products/add" );
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( e );
        }
    }

    @Override
    public ProductDTO findProductByNameAndImgAndPrice ( String name, String url, String price ) {
        AtomicReference<ProductDTO> products = new AtomicReference<> ( );
        httpClient = HttpClient.newHttpClient ( );
        String body = "";

        HttpRequest request = HttpRequest.newBuilder ( )
                .uri ( URI.create ( "http://localhost:6060/api/products/findByName/"+name+"/"+url+"/"+price ) )
                .GET (  )
                .header ( "Content-Type", "application/json" )
                .build ( );
        httpClient.sendAsync ( request, HttpResponse.BodyHandlers.ofString ( ) )
                .thenApply ( HttpResponse::body )
                .thenAccept ( bd -> {
                    try {
                        products.set ( mapper.readValue ( bd, ProductDTO.class ) );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException ( e );
                    }
                } )
                .exceptionally ( err -> {
                    logger.error ( "Error processing {}", err.getMessage ( ) );
                    return null;
                } )
                .join ( );
        return products.get ( );
    }


    private List<ProductDTO> postRequest ( ProductDTO productDTO, ClientDTO client, String url ) throws JsonProcessingException {
        AtomicReference<List<ProductDTO>> products = new AtomicReference<> ( );
        httpClient = HttpClient.newHttpClient ( );
        String body = "";
        if (productDTO != null)
            body = mapper.writeValueAsString ( productDTO );
        else if (client != null)
            body = mapper.writeValueAsString ( clientDTO );

        HttpRequest request = HttpRequest.newBuilder ( )
                .uri ( URI.create ( url ) )
                .POST ( HttpRequest.BodyPublishers.ofString ( body ) )
                .header ( "Content-Type", "application/json" )
                .build ( );
        httpClient.sendAsync ( request, HttpResponse.BodyHandlers.ofString ( ) )
                .thenApply ( HttpResponse::body )
                .thenAccept ( bd -> {
                    try {
                        products.set ( List.of ( mapper.readValue ( bd, ProductDTO[].class ) ) );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException ( e );
                    }
                } )
                .exceptionally ( err -> {
                    logger.error ( "Error processing {}", err.getMessage ( ) );
                    return null;
                } )
                .join ( );
        return products.get ( );
    }

    private List<ProductDTO> sendRequest ( String url ) {
        AtomicReference<List<ProductDTO>> productDTO = new AtomicReference<> ( );
        httpClient = HttpClient.newHttpClient ( );
        HttpRequest request = HttpRequest.newBuilder ( )
                .uri ( URI.create ( url ) )
                .GET ( )
                .header ( "Content-Type", "application/json" )
                .build ( );
        httpClient.sendAsync ( request, HttpResponse.BodyHandlers.ofString ( ) )
                .thenApply ( HttpResponse::body )
                .thenAccept ( body -> {
                    try {
                        productDTO.set ( List.of ( mapper.readValue ( body, ProductDTO[].class ) ) );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException ( e );
                    }
                } )
                .exceptionally ( err -> {
                    logger.error ( "Error processing {}", err.getMessage ( ) );
                    return null;
                } )
                .join ( );
        return productDTO.get ( );
    }
}
