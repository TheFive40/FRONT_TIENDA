package io.github.thefive40.tienda_front.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
/**
 * UserService is a service class responsible for managing user-related operations.
 * It implements {@link UserRepository} and interacts with a backend API
 * to fetch, update, and retrieve user information asynchronously using HTTP requests.
 */
@Service
public class UserService implements UserRepository {
    /**
     * ObjectMapper for JSON serialization and deserialization.
     */
    private ObjectMapper mapper;

    /**
     * Logger instance for logging events and errors.
     */
    private Logger logger = LoggerFactory.getLogger ( UserService.class );

    /**
     * Constructs an instance of {@code UserService} with a given ObjectMapper.
     *
     * @param mapper the {@link ObjectMapper} used for JSON processing.
     */
    public UserService ( ObjectMapper mapper ) {
        this.mapper = mapper;
    }
    /**
     * Retrieves a user by their email address.
     *
     * @param email the user's email.
     * @return a {@link ClientDTO} representing the user.
     */
    @Override
    public ClientDTO getUserByEmail ( String email ) {
        return sendRequest ( "http://localhost:6060/api/users/email/" + email );
    }
    /**
     * Retrieves all users from the backend.
     *
     * @return a {@link List} of {@link ClientDTO} objects representing all users.
     */
    public List<ClientDTO> findAll () {
        return getClients ( "http://localhost:6060/api/users/all" );
    }
    /**
     * Updates a user's information in the backend.
     *
     * @param clientDTO the {@link ClientDTO} containing updated user information.
     * @throws JsonProcessingException if JSON serialization fails.
     */
    public void update ( ClientDTO clientDTO ) throws JsonProcessingException {
        postRequest ( "http://localhost:6060/api/users/update", clientDTO );
    }
    /**
     * Retrieves users by their last name.
     *
     * @param lastname the user's last name.
     * @return a {@link List} of {@link ClientDTO} objects representing the matching users.
     */
    public List<ClientDTO> findByLastName(String lastname){
        return getClients ( "http://localhost:6060/api/users/findByLastName/" + lastname.replace ( " ", "_" ) );
    }
    /**
     * Retrieves a user's password by their email address.
     *
     * @param email the user's email.
     * @return the password as a {@link String}.
     */
    @Override
    public String getPasswordByEmail ( String email ) {
        return sendRequest ( "" ).getPassword ( );
    }
    /**
     * Sends an HTTP GET request to retrieve a single user.
     *
     * @param url the backend API endpoint.
     * @return a {@link ClientDTO} representing the user.
     */
    private ClientDTO sendRequest ( String url ) {
        AtomicReference<ClientDTO> user = new AtomicReference<> ( );
        HttpRequest request = HttpRequest.newBuilder ( )
                .header ( "Content-Type", "application/json" )
                .uri ( URI.create ( url ) )
                .GET ( )
                .build ( );
        HttpClient client = HttpClient.newHttpClient ( );
        client.sendAsync ( request, HttpResponse.BodyHandlers.ofString ( ) )
                .thenApply ( HttpResponse::body )
                .thenAcceptAsync ( e -> {
                    try {
                        user.set ( mapper.readValue ( e, ClientDTO.class ) );
                    } catch (JsonProcessingException ex) {
                        logger.error("The server returned an empty response. Please verify that the endpoint {} is working correctly.", url);
                    }
                } ).exceptionally ( e -> {
                    logger.error ( Marker.ANY_MARKER, "Error while making the request for the user: " + e.getMessage ( ), e );
                    return null;
                } ).join ( );
        return user.get ( );
    }
    /**
     * Sends an HTTP POST request to update user data.
     *
     * @param url       the backend API endpoint.
     * @param clientDTO the {@link ClientDTO} containing user data to update.
     * @throws JsonProcessingException if JSON serialization fails.
     */
    public void postRequest ( String url, ClientDTO clientDTO ) throws JsonProcessingException {
        HttpClient client = HttpClient.newHttpClient ( );
        String json = mapper.writeValueAsString ( clientDTO );
        HttpRequest request = HttpRequest.newBuilder ( )
                .POST ( HttpRequest.BodyPublishers.ofString ( json ) )
                .uri ( URI.create ( url ) )
                .header ( "Content-Type", "application/json" )
                .build ( );

        client.sendAsync ( request, HttpResponse.BodyHandlers.ofString ( ) )
                .thenApply ( HttpResponse::statusCode )
                .thenAccept ( e -> {
                    logger.info ( "Response " + e );
                } )
                .exceptionally ( e -> {
                    logger.error ( Marker.ANY_MARKER, "Error while making the request: " + e.getMessage ( ), e );
                    return null;
                } )
                .join ( );
    }
    /**
     * Sends an HTTP GET request to retrieve multiple users.
     *
     * @param url the backend API endpoint.
     * @return a {@link List} of {@link ClientDTO} objects representing the users.
     */
    private List<ClientDTO> getClients ( String url ) {
        AtomicReference<List<ClientDTO>> clients = new AtomicReference<> ( );
        HttpClient client = HttpClient.newHttpClient ( );
        HttpRequest request = HttpRequest.newBuilder ( )
                .uri ( URI.create ( url ) )
                .header ( "Content-Type", "application/json" )
                .GET ( )
                .build ( );
        client.sendAsync ( request, HttpResponse.BodyHandlers.ofString ( ) )
                .thenApply ( HttpResponse::body )
                .thenAcceptAsync ( e -> {
                    try {
                        ClientDTO[] clientDTO = mapper.readValue ( e, ClientDTO[].class );
                        clients.set ( List.of ( clientDTO ) );
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException ( ex );
                    }
                } )
                .exceptionally ( e -> null )
                .join ( );
        return clients.get ( );
    }

    /**
     * Retrieves a user's email by their phone number.
     *
     * @param phone the user's phone number.
     * @return the email as a {@link String}.
     */
    @Override
    public String getEmailByPhone ( String phone ) {
        return sendRequest ( "" ).getEmail ( );
    }
    /**
     * Retrieves users by their first name.
     *
     * @param name the user's first name.
     * @return a {@link List} of {@link ClientDTO} objects representing the matching users.
     */
    @Override
    public List<ClientDTO> findClientsByName ( String name ) {
        AtomicReference<List<ClientDTO>> clientDTO = new AtomicReference<> ( );
        HttpClient httpClient = HttpClient.newHttpClient ( );
        HttpRequest request = HttpRequest.newBuilder ( )
                .uri ( URI.create ( "http://localhost:6060/api/users/findByName/" + name.replace ( " ", "_" ) ) )
                .GET ( )
                .header ( "Content-Type", "application/json" )
                .build ( );
        httpClient.sendAsync ( request, HttpResponse.BodyHandlers.ofString ( ) )
                .thenApply ( HttpResponse::body )
                .thenAccept ( body -> {
                    try {
                        clientDTO.set ( List.of ( mapper.readValue ( body, ClientDTO[].class ) ) );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException ( e );
                    }
                } )
                .exceptionally ( err -> {
                    logger.error ( "Error processing {}", err.getMessage ( ) );
                    return null;
                } )
                .join ( );
        return clientDTO.get ( );
    }
}
