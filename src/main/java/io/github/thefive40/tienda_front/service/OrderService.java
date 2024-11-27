package io.github.thefive40.tienda_front.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thefive40.tienda_front.model.dto.OrderDTO;
import io.github.thefive40.tienda_front.model.dto.ProductDTO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
/**
 * OrderService is a service component responsible for interacting with the backend
 * to fetch order data based on specific criteria. It uses Java's HTTP client for asynchronous
 * communication and Jackson for JSON processing.
 */
@Component
public class OrderService {

    /**
     * Logger instance for logging events and errors.
     */
    private final Logger logger;

    /**
     * ObjectMapper for parsing JSON responses from the backend.
     */
    private final ObjectMapper mapper;

    /**
     * HTTP client for sending asynchronous requests.
     */
    private HttpClient httpClient;

    /**
     * Constructs an instance of {@code OrderService} with the provided logger and mapper.
     *
     * @param logger the logger instance for logging.
     * @param mapper the {@link ObjectMapper} instance for JSON serialization and deserialization.
     */
    public OrderService ( Logger logger, @Qualifier("mapper") ObjectMapper mapper ) {
        this.logger = logger;
        this.mapper = mapper;
    }
    /**
     * Fetches a list of orders based on the specified city.
     *
     * @param city the name of the city to filter orders.
     * @return a {@link List} of {@link OrderDTO} objects representing the orders in the specified city.
     */
    public List<OrderDTO> findByCity(String city){
        return sendRequest ( "http://localhost:6060/city/" + city.replace ( " ", "_" ) );
    }
    /**
     * Sends an HTTP GET request to the specified URL and processes the response.
     *
     * @param url the URL to send the request to.
     * @return a {@link List} of {@link OrderDTO} objects obtained from the response.
     */
    protected List<OrderDTO> sendRequest(String url){
        AtomicReference<List<OrderDTO>> productDTO = new AtomicReference<> ( );
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
                        productDTO.set ( List.of ( mapper.readValue ( body, OrderDTO[].class ) ) );
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
