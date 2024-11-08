package io.github.thefive40.tienda_front.notifications.information;

import javafx.scene.control.Alert;

public class AlertRegister {

    public void showRegisterClient () {
        Alert alert = new Alert ( Alert.AlertType.INFORMATION );
        alert.setTitle ( "¡Registro Exitoso!" );
        alert.setContentText ( "¡Cliente registrado con éxito en la base de datos!\n\n"
                + "Puede ahora gestionar la información del cliente desde el panel de administración. "
                + "Recuerde verificar los datos antes de proceder con cualquier acción." );
        alert.setHeaderText ( "¡Registrado!" );
        alert.show ();
    }

    public void showUpdateClient () {
        Alert alert = new Alert ( Alert.AlertType.INFORMATION );
        alert.setTitle ( "Actualización Exitosa!" );
        alert.setContentText ( "Los datos del cliente se han actualizado con éxito en la base de datos!\n\n"
                + "Puede verificar los cambios en el panel de administración." );
        alert.setHeaderText ( "Actualizado!" );
        alert.show ();
    }

}
