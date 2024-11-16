package io.github.thefive40.tienda_front.controller.main;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.thefive40.tienda_front.model.dto.ProductDTO;
import io.github.thefive40.tienda_front.service.ProductService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class FindProductController implements Initializable {

    @FXML
    private VBox containerProducts;
    @FXML
    private ListView<String> cartListView;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TitledPane titledCarrito;
    @FXML
    private Accordion cartAccordion;

    @Qualifier("stage")
    @Autowired
    private Stage stage;

    private final ProductService productService;

    private HomeController homeController;
    @Autowired
    private ApplicationContext context;


    public FindProductController (  ProductService productService ) {
        this.productService = productService;
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        homeController = context.getBean ( "HomeController", HomeController.class );
        String searchText = homeController.getSearchTextField ( ).getText ( );
        List<ProductDTO> productDTOS = productService.getProducts ( );
        List<ProductDTO> products =  productDTOS.stream ( ).
                filter ( e -> e.getName ().toUpperCase ().contains ( searchText.toUpperCase ( ) ) ).toList ();
        cartListView.itemsProperty ().bindBidirectional ( homeController.getCartListView ().itemsProperty () );
        cartListView.selectionModelProperty ().bindBidirectional ( homeController.getCartListView ().selectionModelProperty () );
        quantityTextField.textProperty ().bindBidirectional ( homeController.getQuantityTextField ().textProperty () );
        homeController.getUtility ().fillProducts ( containerProducts, products );
        titledCarrito.setExpanded ( true );
        cartAccordion.setExpandedPane ( titledCarrito );

    }

    public void handleEstadistica ( ActionEvent event ) {
        homeController.handleEstadistica ();
    }

    public void handleConfig ( ActionEvent event ) {
        homeController.handleConfig ();
    }
    public void handleInicio(){
        stage.setScene ( new Scene ( context.getBean ( "homeParent", AnchorPane.class ) ) );
    }

    public void handleDevelopers ( ActionEvent event ) {
        homeController.handleDevelopers ();
    }

    public void handleContact ( ActionEvent event ) {
        homeController.handleContact ();
    }

    public void handleMenuProduct ( ActionEvent event ) {
        homeController.handleMenuProduct ();
    }

    public void handlePurchase ( ActionEvent event ) {
        homeController.handlePurchase ();
    }

    public void handleCart ( ActionEvent event ) throws JsonProcessingException {
        homeController.handleCart ( event );
    }

    public void handlePay ( ActionEvent event ) throws JsonProcessingException {
        homeController.handleCart ( event );
    }

    public void handleItemClicked ( MouseEvent mouseEvent ) {
        homeController.handleItemClicked ();
    }

    public void handleAddCart ( ActionEvent event ) throws JsonProcessingException {
        homeController.handleAddCart ();
    }

    public void handleRemoveCart ( ActionEvent event ) {
     homeController.handleRemoveCart ();
    }

    public void handleAfter ( ActionEvent event ) {
        homeController.handleAfter ();
    }

    public void handleBefore ( ActionEvent event ) {
        homeController.handleBefore ();
    }

    public void handleMenuClient ( ActionEvent event ) {
        homeController.handleMenuClient ( event );
    }

    public void handleLogout ( ActionEvent event ) {
        homeController.handleLogout (  );
    }
}
