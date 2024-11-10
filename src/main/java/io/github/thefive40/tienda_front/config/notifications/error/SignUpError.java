package io.github.thefive40.tienda_front.config.notifications.error;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
public class SignUpError {
    private Alert alert;

    public SignUpError () {
        alert = new Alert ( Alert.AlertType.ERROR );
        alert.setTitle ( "Error al registrar" );
        alert.setHeaderText ( "No se pudo registrar al usuario" );
        alert.setContentText ( "Por favor, intente nuevamente" );
        Stage alertStage = (Stage) alert.getDialogPane ( ).getScene ( ).getWindow ( );
        String RUTA = "/static/media/images/icon/icon-error.png";
        Image icon = new Image ( Objects.requireNonNull ( getClass ( ).getResourceAsStream ( RUTA ) ) );
        alertStage.getIcons ( ).add ( icon );
    }

    public void show () {
        alert.show ( );
    }
}
