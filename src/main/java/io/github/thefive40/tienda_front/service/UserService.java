package io.github.thefive40.tienda_front.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thefive40.tienda_front.model.dto.UserDTO;
import io.github.thefive40.tienda_front.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class UserService implements UserRepository {
    private ObjectMapper mapper;
    private UserDTO user;
    private Logger logger = LoggerFactory.getLogger ( UserService.class );

    public UserService ( ObjectMapper mapper ) {
        this.mapper = mapper;
    }

    @Override
    public UserDTO getUserByEmail ( String email ) {
        return sendRequest ( "http://localhost:6060/api/users/email/" + email );
    }

    @Override
    public String getPasswordByEmail ( String email ) {
        return sendRequest ( "" ).getPassword ( );
    }
    private UserDTO sendRequest(String url){
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
                        user = mapper.readValue ( e, UserDTO.class );
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException ( ex );
                    }
                } ).exceptionally ( e -> {
                    logger.error(Marker.ANY_MARKER, "Error while making the request for the user: " + e.getMessage(), e);
                    return null;
                } ).join ( );
        return user;
    }
    @Override
    public String getEmailByPhone ( String phone ) {
        return sendRequest ( "" ).getEmail ();
    }
}
