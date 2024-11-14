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

@Component
public class OrderService {

    private final Logger logger;
    private final ObjectMapper mapper;
    private HttpClient httpClient;

    public OrderService ( Logger logger, @Qualifier("mapper") ObjectMapper mapper ) {
        this.logger = logger;
        this.mapper = mapper;
    }

    public List<OrderDTO> findByCity(String city){
        return sendRequest ( "http://localhost:6060/city/" + city.replace ( " ", "_" ) );
    }

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
