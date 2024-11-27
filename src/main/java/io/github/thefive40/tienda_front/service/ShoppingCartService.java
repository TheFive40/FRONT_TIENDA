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
/**
 * ShoppingCartService is a service class that implements {@link ShoppingCartRepository}.
 * It provides methods to manage shopping cart operations, including saving,
 * finding, updating, and removing items from a shopping cart by interacting with
 * a backend API through HTTP requests.
 */
@Service
public class ShoppingCartService implements ShoppingCartRepository {
    /**
     * ObjectMapper for JSON serialization and deserialization.
     */
    private final ObjectMapper mapper;
    /**
     * Logger instance for logging events and errors.
     */
    private final Logger logger;
    /**
     * HTTP client for making asynchronous requests to the backend.
     */
    private HttpClient httpClient;
    /**
     * Constructs a ShoppingCartService instance with dependencies injected.
     *
     * @param mapper the {@link ObjectMapper} for handling JSON.
     * @param logger the {@link Logger} for logging events and errors.
     */
    public ShoppingCartService ( @Qualifier("mapper") ObjectMapper mapper, Logger logger ) {
        this.mapper = mapper;
        this.logger = logger;
    }
    /**
     * Sends a POST request to save a shopping cart to the backend.
     *
     * @param cartDTO the {@link ShoppingCartDTO} representing the shopping cart to save.
     * @param url     the URL of the backend endpoint.
     * @return a {@link List} of {@link ShoppingCartDTO} objects.
     * @throws JsonProcessingException if JSON serialization fails.
     */
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

    /**
     * Saves a shopping cart to the backend.
     *
     * @param shoppingCartDTO the {@link ShoppingCartDTO} to save.
     */
    @Override
    public void save ( ShoppingCartDTO shoppingCartDTO ) {
        try {
            saveRequest ( shoppingCartDTO, "http://localhost:6060/api/cart/save" );
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( e );
        }
    }

    /**
     * Finds a shopping cart by a client.
     *
     * @param clientDTO the {@link ClientDTO} representing the client.
     * @return the {@link ShoppingCartDTO} for the specified client.
     * @throws JsonProcessingException if JSON serialization fails.
     */
    @Override
    public ShoppingCartDTO findByClient ( ClientDTO clientDTO ) throws JsonProcessingException {
        AtomicReference<ShoppingCartDTO> shoppingCart = new AtomicReference<> ( );
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
    /**
     * Finds an item in a shopping cart by the product and the shopping cart.
     *
     * @param shoppingCartDTO the {@link ShoppingCartDTO}.
     * @param productDTO       the {@link ProductDTO}.
     * @return the {@link ItemCartDTO} representing the found item.
     * @throws JsonProcessingException if JSON serialization fails.
     */
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
    /**
     * Finds all shopping carts (not implemented).
     *
     * @return an empty {@link List}.
     */
    @Override
    public List<ShoppingCartDTO> findAll () {
        return List.of ( );
    }
    /**
     * Removes an item from a shopping cart.
     *
     * @param itemCartDTO the {@link ItemCartDTO} to remove.
     */
    public void remove(ItemCartDTO itemCartDTO){
        httpClient = HttpClient.newHttpClient ( );
        String body = "";
        try {
            body = mapper.writeValueAsString ( itemCartDTO );
            System.out.println (body );
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
    /**
     * Updates an item in the shopping cart.
     *
     * @param itemCartDTO the {@link ItemCartDTO} to update.
     */
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
