package io.github.thefive40.tienda_front.config.notifications.error;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthError {
    private Alert alert;

    public AuthError () {
        alert = new Alert ( Alert.AlertType.ERROR );
        alert.setTitle ( "Error de Autenticación" );
        alert.setHeaderText ( "No se pudo iniciar sesión." );
        alert.setContentText ( "Verifica tu correo electrónico y contraseña.");
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        String RUTA = "/static/media/images/icon/icon-error.png";
        Image icon = new Image ( Objects.requireNonNull ( getClass ( ).getResourceAsStream ( RUTA ) ) );
        alertStage.getIcons().add(icon);
    }

    public void show () {
        alert.show ( );
    }
}
