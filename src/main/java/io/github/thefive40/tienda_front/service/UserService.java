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

@Service
public class UserService implements UserRepository {
    private ObjectMapper mapper;
    private ClientDTO user;
    private Logger logger = LoggerFactory.getLogger ( UserService.class );

    public UserService ( ObjectMapper mapper ) {
        this.mapper = mapper;
    }

    @Override
    public ClientDTO getUserByEmail ( String email ) {
        return sendRequest ( "http://localhost:6060/api/users/email/" + email );
    }

    public List<ClientDTO> findAll () {
        return getClients ( "http://localhost:6060/api/users/all" );
    }

    public void update ( ClientDTO clientDTO ) throws JsonProcessingException {
        postRequest ( "http://localhost:6060/api/users/update", clientDTO );
    }

    @Override
    public String getPasswordByEmail ( String email ) {
        return sendRequest ( "" ).getPassword ( );
    }

    private ClientDTO sendRequest ( String url ) {
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
                        user = mapper.readValue ( e, ClientDTO.class );
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException ( ex );
                    }
                } ).exceptionally ( e -> {
                    logger.error ( Marker.ANY_MARKER, "Error while making the request for the user: " + e.getMessage ( ), e );
                    return null;
                } ).join ( );
        return user;
    }

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

    @Override
    public String getEmailByPhone ( String phone ) {
        return sendRequest ( "" ).getEmail ( );
    }
}
