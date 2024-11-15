package io.github.thefive40.tienda_front.controller.main.menu.purchase.popups;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.thefive40.tienda_front.controller.main.menu.purchase.PurchaseController;
import io.github.thefive40.tienda_front.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class PurchaseEditController  implements Initializable {
    @FXML
    private Button actualizarButton;

    @FXML
    private GridPane mainGrid;

    @FXML
    private TextField codigoField;

    @FXML
    private TextField correoField;

    @FXML
    private TextField totalField;

    @FXML
    private ComboBox<?> estadoComboBox;

    @FXML
    private TextField direccionField;
    @Autowired
    private ApplicationContext context;

    private PurchaseController purchaseController;
    @Autowired
    private UserService userService;

    @FXML
    void handleUpdate( ActionEvent event) throws JsonProcessingException {
        var client = purchaseController.getClientOrder ();
        var currentOrder = purchaseController.getCurrentOrder ();
        client.setEmail ( correoField.getText ( ) );
        currentOrder.setZipCode ( codigoField.getText ( ) );
        currentOrder.setAddress ( direccionField.getText (  ));
        currentOrder.setCity ( totalField.getText (  ) );
        client.getOrders ().remove ( currentOrder );
        client.getOrders ().add ( currentOrder );
        userService.update ( client );
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        purchaseController = context.getBean ( "PurchaseController", PurchaseController.class );
        var order = purchaseController.getCurrentOrder ();
        correoField.setText ( purchaseController.getClientOrder ().getEmail () );
        codigoField.setText ( order.getZipCode () );
        direccionField.setText ( order.getAddress () );
        totalField.setText ( String.valueOf ( order.getTotal () ) );
    }
}
