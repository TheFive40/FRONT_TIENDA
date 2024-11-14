package io.github.thefive40.tienda_front.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thefive40.tienda_front.model.dto.*;
import io.github.thefive40.tienda_front.repository.ShoppingCartRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ShoppingCartService implements ShoppingCartRepository {

    private final ObjectMapper mapper;
    private final Logger logger;
    private HttpClient httpClient;

    public ShoppingCartService ( @Qualifier("mapper") ObjectMapper mapper, Logger logger ) {
        this.mapper = mapper;
        this.logger = logger;
    }

    private List<ShoppingCartDTO> saveRequest ( ShoppingCartDTO cartDTO, String url ) throws JsonProcessingException {
        AtomicReference<List<ShoppingCartDTO>> products = new AtomicReference<> ( );
        httpClient = HttpClient.newHttpClient ( );
        String body = "";
        if (cartDTO != null)
            body = mapper.writeValueAsString ( cartDTO );
        HttpRequest request = HttpRequest.newBuilder ( )
                .uri ( URI.create ( url ) )
                .POST ( HttpRequest.BodyPublishers.ofString ( body ) )
                .header ( "Content-Type", "application/json" )
                .build ( );
        httpClient.sendAsync ( request, HttpResponse.BodyHandlers.ofString ( ) )
                .thenApply ( HttpResponse::body )
                .thenAccept ( bd -> {
                    logger.info ( "Request proccesing" );
                } )
                .exceptionally ( err -> {
                    logger.error ( "Error processing {}", err.getMessage ( ) );
                    return null;
                } )
                .join ( );
        return products.get ( );
    }

    @Override
    public void save ( ShoppingCartDTO shoppingCartDTO ) {
        try {
            saveRequest ( shoppingCartDTO, "http://localhost:6060/api/cart/save" );
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( e );
        }
    }


    @Override
    public ShoppingCartDTO findByClient ( ClientDTO clientDTO ) throws JsonProcessingException {
        AtomicReference<ShoppingCartDTO> shoppingCart = new AtomicReference<> ( );
        System.out.println ( clientDTO.getEmail ( ) );
        httpClient = HttpClient.newHttpClient ( );
        String body = "";
        body = mapper.writeValueAsString ( clientDTO );
        HttpRequest request = HttpRequest.newBuilder ( )
                .uri ( URI.create ( "http://localhost:6060/api/cart/findByClient" ) )
                .POST ( HttpRequest.BodyPublishers.ofString ( body ) )
                .header ( "Content-Type", "application/json" )
                .build ( );
        httpClient.sendAsync ( request, HttpResponse.BodyHandlers.ofString ( ) )
                .thenApply ( HttpResponse::body )
                .thenAccept ( bd -> {
                    logger.info ( "Request proccesing" );
                    try {
                        shoppingCart.set ( mapper.readValue ( bd, ShoppingCartDTO.class ) );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException ( e );
                    }
                } )
                .exceptionally ( err -> {
                    logger.error ( "Error processing {}", err.getMessage ( ) );
                    return null;
                } )
                .join ( );
        return shoppingCart.get ( );
    }

    public ItemCartDTO findByProductAndShoppingCart ( ShoppingCartDTO shoppingCartDTO, ProductDTO productDTO ) throws JsonProcessingException {
        RequestProductShopDto requestProductShopDto = new RequestProductShopDto ( );
        requestProductShopDto.setShoppingCart ( shoppingCartDTO );
        requestProductShopDto.setProduct ( productDTO );
        AtomicReference<ItemCartDTO> items = new AtomicReference<> ( );
        httpClient = HttpClient.newHttpClient ( );
        String body = mapper.writeValueAsString ( requestProductShopDto );

        HttpRequest request = HttpRequest.newBuilder ( )
                .uri ( URI.create ( "http://localhost:6060/api/cart/findItemCart" ) )
                .POST ( HttpRequest.BodyPublishers.ofString ( body ) )
                .header ( "Content-Type", "application/json" )
                .build ( );

        httpClient.sendAsync ( request, HttpResponse.BodyHandlers.ofString ( ) )
                .thenApply ( HttpResponse::body )
                .thenAccept ( responseBody -> {
                    logger.info ( "Request processing" );
                    try {
                        ItemCartDTO itemCartDTO = mapper.readValue ( responseBody, ItemCartDTO.class );
                        items.set ( itemCartDTO );
                    } catch (JsonProcessingException e) {
                        logger.error ( "Error processing JSON response: {}", e.getMessage ( ) );
                    }
                } )
                .exceptionally ( err -> {
                    logger.error ( "Error processing request: {}", err.getMessage ( ) );
                    return null;
                } )
                .join ( );

        return items.get ( );
    }

    @Override
    public List<ShoppingCartDTO> findAll () {
        return List.of ( );
    }

    public void remove(ItemCartDTO itemCartDTO){
        httpClient = HttpClient.newHttpClient ( );
        String body = "";
        try {
            body = mapper.writeValueAsString ( itemCartDTO );
            System.out.println (body
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( e );
        }
        HttpRequest request = HttpRequest.newBuilder ( )
                .uri ( URI.create ( "http://localhost:6060/api/cart/remove" ) )
                .POST ( HttpRequest.BodyPublishers.ofString ( body ) )
                .header ( "Content-Type", "application/json" )
                .build ( );
        httpClient.sendAsync ( request, HttpResponse.BodyHandlers.ofString ( ) )
                .thenApply ( HttpResponse::body )
                .thenAccept ( bd -> {
                    logger.info ( "Request proccesing" );
                } )
                .exceptionally ( err -> {
                    logger.error ( "Error processing {}", err.getMessage ( ) );
                    return null;
                } )
                .join ( );
    }

    @Override
    public void updateItemCart ( ItemCartDTO itemCartDTO ) {
        httpClient = HttpClient.newHttpClient ( );
        String body = "";
        try {
            body = mapper.writeValueAsString ( itemCartDTO );
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( e );
        }
        HttpRequest request = HttpRequest.newBuilder ( )
                .uri ( URI.create ( "http://localhost:6060/api/cart/update" ) )
                .POST ( HttpRequest.BodyPublishers.ofString ( body ) )
                .header ( "Content-Type", "application/json" )
                .build ( );
        httpClient.sendAsync ( request, HttpResponse.BodyHandlers.ofString ( ) )
                .thenApply ( HttpResponse::body )
                .thenAccept ( bd -> {
                    logger.info ( "Request proccesing" );
                } )
                .exceptionally ( err -> {
                    logger.error ( "Error processing {}", err.getMessage ( ) );
                    return null;
                } )
                .join ( );

    }
}
