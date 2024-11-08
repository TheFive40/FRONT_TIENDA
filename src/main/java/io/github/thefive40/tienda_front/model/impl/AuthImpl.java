package io.github.thefive40.tienda_front.model.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thefive40.tienda_front.TiendaFrontApplication;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.notifications.information.AlertRegister;
import io.github.thefive40.tienda_front.repository.AuthRepository;
import io.github.thefive40.tienda_front.service.EmailService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Getter
@Setter
public class AuthImpl implements AuthRepository {
    private volatile AtomicBoolean commit = new AtomicBoolean ( );
    private ObjectMapper mapper;
    private EmailService emailService;

    public AuthImpl ( EmailService emailService ) {
        this.emailService = emailService;
    }

    @Autowired
    private void setMapper ( ObjectMapper mapper ) {
        this.mapper = mapper;
    }

    private Logger logger = LoggerFactory.getLogger ( TiendaFrontApplication.class );

    @Override
    public boolean sendLogin ( String email, String password ) {
        ClientDTO clientDTO = new ClientDTO ( email, password );
        return sendRequest ( clientDTO, true );
    }

    protected boolean sendRequest ( ClientDTO clientDTO, boolean isLogin ) {
        String page = (isLogin) ? "login" : "signup";
        AtomicBoolean result = new AtomicBoolean ( false );
        HttpClient client = HttpClient.newHttpClient ( );
        String json;
        try {
            json = mapper.writeValueAsString ( clientDTO );
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( e );
        }
        HttpRequest request = HttpRequest.newBuilder ( )
                .header ( "Content-Type", "application/json" )
                .uri ( URI.create ( "http://localhost:6060/auth/" + page ) )
                .POST ( HttpRequest.BodyPublishers.ofString ( json ) )
                .build ( );
        CompletableFuture<HttpResponse<String>> response = client.sendAsync ( request, HttpResponse.BodyHandlers.ofString ( ) );
        response.thenApply ( HttpResponse::statusCode )
                .thenAccept ( statusCode -> {
                    if (statusCode == 200) {
                        logger.info ( "¡Solicitud enviada al servidor!" );
                        result.set ( true );
                    } else {
                        logger.error ( "Ha ocurrido un error, el codigo de respuesta es {}!", statusCode );
                    }
                } )
                .exceptionally ( err -> {
                    logger.error ( "¡Ha ocurrido un error, no se ha podido encontrar informacion en la base de datos!" );
                    return null;
                } ).join ( );
        return result.get ( );
    }

    @Override
    public void sendRegistration ( ClientDTO clientDTO ) {
        sendRequest ( clientDTO, false );
    }

    @Override
    public void commit () {
        commit.set ( true );
    }

    @Override
    public boolean isCommit () {
        return commit.get ( );
    }

    @Override
    public void uncommit () {
        commit.set ( false );
    }

}
