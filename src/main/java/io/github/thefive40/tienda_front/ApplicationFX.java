package io.github.thefive40.tienda_front;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Getter
@Setter
public class ApplicationFX extends Application {

    @Override
    public void start ( Stage stage ) throws IOException {
        var context = SpringApplication.run ( TiendaFrontApplication.class );
        FXMLLoader loginFXML = context.getBean ( "loginFXML", FXMLLoader.class );
        stage = context.getBean ( "stage", Stage.class );
        Parent loginParent = loginFXML.load ( );
        stage.setTitle ( "Tienda Online" );
        stage.setScene ( new javafx.scene.Scene ( loginParent ) );
        stage.show ( );
    }
}
