package io.github.thefive40.tienda_front.controller.main.menu.purchase.popups;

import io.github.thefive40.tienda_front.controller.main.menu.purchase.PurchaseController;
import io.github.thefive40.tienda_front.model.dto.DetailOrderDTO;
import io.github.thefive40.tienda_front.model.dto.OrderDTO;
import io.github.thefive40.tienda_front.service.UtilityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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
public class PurchaseDetailsController implements Initializable {
    private final UtilityService<DetailOrderDTO> utilityService;
    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button detailImageLabel;

    @FXML
    private Button detailNameLabel;

    @FXML
    private TextField txtTotalPage;

    @FXML
    private Label detailProduct5;

    @FXML
    private Label detailProduct4;

    @FXML
    private Button detailDescriptionLabel;

    @FXML
    private Label detailProduct3;

    @FXML
    private Label detailProduct2;

    @FXML
    private Label detailProduct1;

    @FXML
    private AnchorPane homeParent;

    @FXML
    private Label detailPrice4;

    @FXML
    private Label detailPrice5;

    @FXML
    private Button btnBefore1;

    @FXML
    private Label detailQuantity3;

    @FXML
    private Label detailPrice1;

    @FXML
    private Label detailQuantity2;

    @FXML
    private Label detailQuantity1;

    @FXML
    private Label detailPrice2;

    @FXML
    private Label labelClientes;

    @FXML
    private VBox vboxContainer;

    @FXML
    private ImageView detailImage1;

    @FXML
    private Label detailPrice3;

    @FXML
    private Button btnAfter1;

    @FXML
    private ImageView detailImage2;

    @FXML
    private ImageView detailImage3;

    @FXML
    private Button detailIdLabel;

    @FXML
    private ImageView detailImage4;

    @FXML
    private Label detailQuantity5;

    @FXML
    private Label detailQuantity4;

    @FXML
    private ImageView detailImage5;

    @FXML
    private ComboBox<?> filterButton;

    @FXML
    private TextField txtPage;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private PurchaseController purchaseController;

    public PurchaseDetailsController ( UtilityService<DetailOrderDTO> utilityService ) {
        this.utilityService = utilityService;
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        txtPage.setText ( "1" );
        purchaseController = context.getBean ( "PurchaseController", PurchaseController.class );
        fillTablePurchase (  purchaseController.getClientDetailOrders () );
    }

    public void handlePressed ( KeyEvent keyEvent ) {
    }

    public void handleSearch ( ActionEvent event ) {
    }

    public void handleBefore ( ActionEvent event ) {
    }

    public void handleAfter ( ActionEvent event ) {
    }


    void fillTablePurchase ( List<DetailOrderDTO> orders ) {
        txtTotalPage.setText ( utilityService.totalPages ( orders ) + "" );
        List<DetailOrderDTO> ordersDTOS = utilityService.getItemsByPage ( Integer.parseInt ( txtPage.getText ( ) ), orders );
        AtomicInteger contador = new AtomicInteger ( 0 );
        vboxContainer.getChildren ( ).forEach ( e -> {
            int count = contador.getAndIncrement ( );
            HBox container = (HBox) e;
            ImageView imageView = (ImageView) container.getChildren ( ).get ( 0 );
            Label productName = (Label) container.getChildren ( ).get ( 1 );
            Label quantity = (Label) container.getChildren ( ).get ( 2 );
            Label unitPrice = (Label) container.getChildren ( ).get ( 3 );
            if (count >= ordersDTOS.size ( )) {
                clearProductsInfo ( imageView, productName, quantity, unitPrice);
            } else {
                DetailOrderDTO order = ordersDTOS.get ( count );
                imageView.setImage ( new Image (order.getProduct ().getImg () ) );
                productName.setText ( String.valueOf ( order.getProduct ().getName () ) );
                unitPrice.setText ( order.getUnitPrice () + "");
                quantity.setText ( order.getAmount () + "" );
                imageView.setPreserveRatio ( false );
                imageView.setSmooth ( false );

            }

        } );
    }

    void clearProductsInfo ( ImageView img, Label productName, Label quantity, Label unitPrice){
        unitPrice.setText ( "" );
        quantity.setText ( "" );
        productName.setText ( "" );
        img.setImage ( null );
    }
}
