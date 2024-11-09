package io.github.thefive40.tienda_front.service;

import io.github.thefive40.tienda_front.controller.main.HomeController;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UtilityService <T> {
    private List<T> items;

    private static final int CLIENTS_PER_PAGE = 5;

    private HomeController homeController;
    private Logger logger;

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

    public T findItemDto ( Button button, int page ) {
        List<T> clientDTOS = getItemsByPage ( page, items );
        String num = button.getId ().charAt ( button.getId ().length ()-1)+ "";
        T clientDTO = null;
        if (isNumber ( num )){
           int userIndex =  Integer.parseInt ( num )-1;
           clientDTO =  clientDTOS.get ( userIndex );
        }
        return clientDTO;
    }

    public void addProductToCart ( Button button ) {
        StringBuilder text = new StringBuilder ( );
        for(var e: button.getParent ( ).getChildrenUnmodifiable ( )){
            if (e instanceof Label lbl && isNumber ( lbl.getText ( ))) {
                text.append ( lbl.getText ( ) ).append ( " " );
            } else if (e instanceof Label lbl && !isNumber ( lbl.getText ( ) )) {
                text.append ( ";" ).append ( lbl.getText ( ) );
            }
        }
        String textString = text.toString ();
        String name = textString.split ( ";" )[1];
        String price = textString.split ( ";" )[2].replace ( ',','.' );
        homeController.getCartListView ().getItems ().addAll ( name + " $" + price );
    }

    public List<T> getItemsByPage ( int numeroPagina, List<T> clients ) {
        int inicio = (numeroPagina - 1) * CLIENTS_PER_PAGE;
        int fin = Math.min ( inicio + CLIENTS_PER_PAGE, clients.size ( ) );
        this.items = clients;
        if (inicio >= clients.size ( )) {
            return new ArrayList<> ( );
        }

        return clients.subList ( inicio, fin );
    }
    public void loadImage( TextField txtImage ){
        FileChooser fileChooser = new FileChooser ( );
        fileChooser.getExtensionFilters ( ).add ( new FileChooser.ExtensionFilter ( "Text Files", "*.jpg",
                "*.png", "*.jpeg" ) );
        Stage stage = (Stage) txtImage.getScene ( ).getWindow ( );
        File file = fileChooser.showOpenDialog ( stage );
        if (file != null) {
            txtImage.setText ( "/static/media/images/products/" + file.getName ( ) );
        }
    }
    public int totalPages ( List<T> clients ) {
        return (int) Math.ceil ( (double) clients.size ( ) / CLIENTS_PER_PAGE );
    }
    @Autowired
    public void setLogger ( Logger logger ) {
        this.logger = logger;
    }
}
