package io.github.thefive40.tienda_front.service;

import io.github.thefive40.tienda_front.controller.main.HomeController;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UtilityService {
    private List<ClientDTO> clients;

    private static final int CLIENTS_PER_PAGE = 5;

    private HomeController homeController;

    @Autowired
    public void inject ( HomeController homeController ) {
        this.homeController = homeController;
    }

    public boolean isNumber ( String text ) {
        try {
            Double.parseDouble ( text );
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    public ClientDTO findClientDto ( Button button, int page ) {
        List<ClientDTO> clientDTOS = getClientsByPage ( page, clients );
        String num = button.getId ().charAt ( button.getId ().length ()-1)+ "";
        ClientDTO clientDTO = null;
        if (isNumber ( num )){
           int userIndex =  Integer.parseInt ( num )-1;
           clientDTO =  clientDTOS.get ( userIndex );
        }
        return clientDTO;
    }

    public void addProductToCart ( Button button ) {
        AtomicReference<String> name = new AtomicReference<> ( );
        AtomicReference<Double> price = new AtomicReference<> ( );
        button.getParent ( ).getChildrenUnmodifiable ( ).forEach ( e -> {
            if (e instanceof Label lbl && isNumber ( lbl.getText ( ) )) {
                price.set ( Double.parseDouble ( lbl.getText ( ) ) );
            } else if (e instanceof Label lbl && !isNumber ( lbl.getText ( ) )) {
                name.set ( lbl.getText ( ) );
            }
        } );
        homeController.getCartListView ( ).getItems ( ).add ( name.get ( ) + " $" + price.get ( ) );
    }

    public List<ClientDTO> getClientsByPage ( int numeroPagina, List<ClientDTO> clients ) {
        int inicio = (numeroPagina - 1) * CLIENTS_PER_PAGE;
        int fin = Math.min ( inicio + CLIENTS_PER_PAGE, clients.size ( ) );
        this.clients = clients;
        if (inicio >= clients.size ( )) {
            return new ArrayList<> ( );
        }

        return clients.subList ( inicio, fin );
    }

    public int totalPages ( List<ClientDTO> clients ) {
        return (int) Math.ceil ( (double) clients.size ( ) / CLIENTS_PER_PAGE );
    }
}
