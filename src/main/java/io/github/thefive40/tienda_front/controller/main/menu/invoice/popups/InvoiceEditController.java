package io.github.thefive40.tienda_front.controller.main.menu.invoice.popups;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.thefive40.tienda_front.controller.main.menu.invoice.InvoiceController;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.model.dto.InvoiceDTO;
import io.github.thefive40.tienda_front.service.UserService;
import io.github.thefive40.tienda_front.service.UtilityService;
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
public class InvoiceEditController implements Initializable {
    @FXML
    private Button actualizarButton;

    @FXML
    private GridPane mainGrid;

    @FXML
    private TextField correoField;

    @FXML
    private TextField impuestoField;

    @FXML
    private TextField totalField;

    @FXML
    private ComboBox<String> estadoComboBox;

    @FXML
    private TextField subtotalField;

    @Autowired
    private ApplicationContext context;

    private InvoiceController invoiceController;

    private InvoiceDTO invoice;

    private ClientDTO currentClient;


    @Autowired
    private UtilityService<InvoiceDTO> utilityService;
    @Autowired
    private UserService userService;


    @FXML
    public void handleUpdate ( ActionEvent event ) throws JsonProcessingException {
        ClientDTO clientDTO = userService.getUserByEmail ( correoField.getText ( ) );
        currentClient.getInvoices ().remove ( invoice );
        if (utilityService.isNumber ( subtotalField.getText ( ) ) && utilityService.isNumber ( impuestoField.getText ( ) )) {
            invoice.setTax ( Double.parseDouble ( impuestoField.getText ( ) ) );
            invoice.setSubTotal ( Double.parseDouble ( subtotalField.getText ( ) ) );
            invoice.setTotal ( invoice.getSubTotal ( ) * invoice.getTax ( ) + invoice.getSubTotal ( ) );
            impuestoField.setText ( invoice.getTax ()+"" );
            subtotalField.setText ( invoice.getSubTotal () +"");
            totalField.setText ( invoice.getTotal () +"");
         }
        invoice.setStatus ( estadoComboBox.getSelectionModel ( ).getSelectedItem ( ).
                equalsIgnoreCase ( "Activo" ) );
        if (!clientDTO.getInvoices ( ).contains ( invoice )) {
            //currentClient.getInvoices ( ).remove ( invoice );
            clientDTO.getInvoices ( ).add ( invoice );
            userService.update ( currentClient );
            userService.update ( clientDTO );
            invoiceController.refresh ();
            return;
        }
        currentClient.getInvoices ().add ( invoice );
        userService.update ( currentClient );
        invoiceController.refresh ();

    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        invoiceController = context.getBean ( "InvoiceController", InvoiceController.class );
        invoice = invoiceController.getCurrentInvoice ( );
        currentClient = invoiceController.getClientDTO ( );
        correoField.setText ( currentClient.getEmail ( ) );
        impuestoField.setText ( invoice.getTax ( ) + "" );
        subtotalField.setText ( invoice.getSubTotal ( ) + "" );
        totalField.setText ( invoice.getTotal ( ) + "" );
        estadoComboBox.getItems ( ).clear ( );
        estadoComboBox.getItems ( ).addAll ( "Activo", "Inactivo" );
        estadoComboBox.getSelectionModel ().select ( 0 );
    }
}
