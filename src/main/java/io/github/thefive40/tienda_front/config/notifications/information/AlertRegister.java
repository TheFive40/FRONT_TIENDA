package io.github.thefive40.tienda_front.config.notifications.information;

import javafx.scene.control.Alert;

public class AlertRegister {

    public void showClientRegister () {
        Alert alert = new Alert ( Alert.AlertType.INFORMATION );
        alert.setTitle ( "¡Registro Exitoso!" );
        alert.setContentText ( "¡Cliente registrado con éxito en la base de datos!\n\n"
                + "Puede ahora gestionar la información del cliente desde el panel de administración. "
                + "Recuerde verificar los datos antes de proceder con cualquier acción." );
        alert.setHeaderText ( "¡Registrado!" );
        alert.show ();
    }
    public void showProductRegister () {
        Alert alert = new Alert ( Alert.AlertType.INFORMATION );
        alert.setTitle ( "¡Registro Exitoso!" );
        alert.setContentText ( "El producto ha sido registrado exitosamente en la base de datos.\n\n"
                + "Ahora puede gestionar la información del producto desde el panel de administración. "
                + "Por favor, asegúrese de revisar los detalles antes de realizar alguna acción adicional." );
        alert.setHeaderText ( "Producto Registrado" );
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

    public void showUpdateProduct(){
        Alert alert = new Alert( Alert.AlertType.INFORMATION);
        alert.setTitle("Operación Exitosa");
        alert.setHeaderText("El producto ha sido actualizado correctamente");
        alert.setContentText("La actualización del producto se ha realizado con éxito. "
                + "Todos los cambios se han guardado correctamente en la base de datos. "
                + "Si deseas realizar más cambios, puedes continuar editando los productos o salir de la ventana. "
                + "Gracias por usar el sistema de gestión de productos.");
        alert.showAndWait();

    }

}
