package io.github.thefive40.tienda_front.controller.main.menu.invoice.popups;

import io.github.thefive40.tienda_front.controller.main.menu.invoice.InvoiceController;
import io.github.thefive40.tienda_front.controller.main.menu.purchase.PurchaseController;
import io.github.thefive40.tienda_front.model.dto.DetailInvoiceDTO;
import io.github.thefive40.tienda_front.service.UtilityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InvoiceDetailsController implements Initializable {

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField txtTotalPage;

    @FXML
    private VBox vboxContainer;

    @FXML
    private TextField txtPage;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private InvoiceController invoiceController;
    @Autowired
    private UtilityService<DetailInvoiceDTO> utilityService;


    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        txtPage.setText ( "1" );
        invoiceController = context.getBean ( "InvoiceController", InvoiceController.class );
        fillTablePurchase ( invoiceController.getCurrentInvoice ().getDetailsInvoice () );
    }

    public void handlePressed ( KeyEvent keyEvent ) {
    }

    public void handleSearch ( ActionEvent event ) {
    }

    public void handleBefore ( ActionEvent event ) {
    }

    public void handleAfter ( ActionEvent event ) {
    }


    void fillTablePurchase ( List<DetailInvoiceDTO> orders ) {
        txtTotalPage.setText ( utilityService.totalPages ( orders ) + "" );
        List<DetailInvoiceDTO> ordersDTOS = utilityService.getItemsByPage ( Integer.parseInt ( txtPage.getText ( ) ), orders );
        AtomicInteger contador = new AtomicInteger ( 0 );
        vboxContainer.getChildren ( ).forEach ( e -> {
            int count = contador.getAndIncrement ( );
            HBox container = (HBox) e;
            ImageView imageView = (ImageView) container.getChildren ( ).get ( 0 );
            Label productName = (Label) container.getChildren ( ).get ( 1 );
            Label quantity = (Label) container.getChildren ( ).get ( 2 );
            Label unitPrice = (Label) container.getChildren ( ).get ( 3 );
            if (count >= ordersDTOS.size ( )) {
                clearProductsInfo ( imageView, productName, quantity, unitPrice );
            } else {
                DetailInvoiceDTO order = ordersDTOS.get ( count );
                imageView.setImage ( new Image ( order.getProduct ( ).getImg ( ) ) );
                productName.setText ( String.valueOf ( order.getProduct ( ).getName ( ) ) );
                unitPrice.setText ( order.getUnitPrice ( ) + "" );
                quantity.setText ( order.getQuantity ( ) + "" );
                imageView.setPreserveRatio ( false );
                imageView.setSmooth ( false );

            }

        } );
    }

    void clearProductsInfo ( ImageView img, Label productName, Label quantity, Label unitPrice ) {
        unitPrice.setText ( "" );
        quantity.setText ( "" );
        productName.setText ( "" );
        img.setImage ( null );
    }
}
