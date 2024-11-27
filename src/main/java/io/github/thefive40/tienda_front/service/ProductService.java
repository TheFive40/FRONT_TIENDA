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
/**
 * ProductService is a service class that implements the {@link ProductRepository} interface,
 * responsible for interacting with the backend API to perform operations related to products.
 * It provides methods to fetch, save, and search for products using HTTP requests.
 */
@Service
public class ProductService implements ProductRepository {

    /**
     * HTTP client used for sending requests to the backend API.
     */
    private HttpClient httpClient;

    /**
     * ObjectMapper for converting objects to JSON and vice versa.
     */
    private final ObjectMapper mapper;
    /**
     * Logger instance for logging events and errors.
     */
    private final Logger logger;

    /**
     * Client information used for product operations.
     */
    private final ClientDTO clientDTO;
    /**
     * Constructs an instance of {@code ProductService} with dependencies injected.
     *
     * @param mapper    the {@link ObjectMapper} instance for JSON processing.
     * @param logger    the {@link Logger} instance for logging.
     * @param clientDTO the {@link ClientDTO} instance representing the client.
     */
    public ProductService ( @Qualifier("mapper") ObjectMapper mapper, Logger logger, ClientDTO clientDTO ) {
        this.mapper = mapper;
        this.logger = logger;
        this.clientDTO = clientDTO;
    }
    /**
     * Fetches a product by its ID.
     *
     * @param id the ID of the product.
     * @return a {@link ProductDTO} representing the product.
     */
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
    /**
     * Fetches products associated with a specific client.
     *
     * @param client the {@link ClientDTO} representing the client.
     * @return a {@link List} of {@link ProductDTO} objects.
     */
    @Override
    public List<ProductDTO> getProductByClient ( ClientDTO client ) {
        try {
            return postRequest ( null, clientDTO, "http://localhost:6060/api/products/findByClient" );
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( e );
        }

    }
    /**
     * Fetches all products.
     *
     * @return a {@link List} of {@link ProductDTO} objects representing all products.
     */
    @Override
    public List<ProductDTO> getProducts () {
        return sendRequest ( "http://localhost:6060/api/products/findAll" );
    }

    /**
     * Finds products by the client's name.
     *
     * @param clientName the name of the client.
     * @return a {@link List} of {@link ProductDTO} objects.
     */
    @Override
    public List<ProductDTO> findProductsByClientName ( String clientName ) {
        return sendRequest ( "http://localhost:6060/api/products/findProductsByClientName/" + clientName.replace ( " ", "_" ) );
    }

    /**
     * Saves a product to the backend.
     *
     * @param productDTO the {@link ProductDTO} object to be saved.
     */
    @Override
    public void save ( ProductDTO productDTO ) {
        try {
            postRequest ( productDTO, null, "http://localhost:6060/api/products/add" );
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( e );
        }
    }

    /**
     * Finds a product by its name, image URL, and price.
     *
     * @param name  the product name.
     * @param url   the product's image URL.
     * @param price the product's price.
     * @return a {@link ProductDTO} object representing the product.
     */
    @Override
    public ProductDTO findProductByNameAndImgAndPrice ( String name, String url, String price ) {
        AtomicReference<ProductDTO> products = new AtomicReference<> ( );
        httpClient = HttpClient.newHttpClient ( );
        String body = "";

        HttpRequest request = HttpRequest.newBuilder ( )
                .uri ( URI.create ( "http://localhost:6060/api/products/findByName/" + name + "/" + url + "/" + price ) )
                .GET ( )
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
    /**
     * Finds products by their name.
     *
     * @param text the product name or a part of it.
     * @return a {@link List} of {@link ProductDTO} objects.
     */
    @Override
    public List<ProductDTO> findByName ( String text ) {
        return sendRequest ( "http://localhost:6060/api/products/findProductName/" + text.replace ( " ","_" ) );
    }

    /**
     * Sends a POST request to the backend.
     *
     * @param productDTO the product data to be sent.
     * @param client     the client data to be sent.
     * @param url        the target URL.
     * @return a {@link List} of {@link ProductDTO} objects.
     * @throws JsonProcessingException if JSON serialization fails.
     */
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
    /**
     * Sends a GET request to the backend.
     *
     * @param url the target URL.
     * @return a {@link List} of {@link ProductDTO} objects.
     */
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
