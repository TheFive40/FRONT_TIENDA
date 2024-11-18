package io.github.thefive40.tienda_front.config;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

@Configuration
@EnableAsync
public class WindowConfig {

    private final ApplicationContext context;

    @Autowired
    public WindowConfig ( ApplicationContext context ) {
        this.context = context;
    }

    @Bean
    @Scope("prototype")
    public AnchorPane homeParent () throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/Home.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load();
    }


    @Bean
    @Scope("prototype")
    public AnchorPane clientParent() throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/client/Client.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load();

    }
    @Bean
    @Scope("prototype")
    public AnchorPane invoiceParent() throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/invoice/Invoice.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load();

    }
    @Bean
    @Scope("prototype")
    public AnchorPane invoiceDetailsParent() throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/popups/DetailsInvoice.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load();
    }
    @Bean
    @Scope("prototype")
    public GridPane invoiceEditParent() throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/invoice/InvoiceEdit.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load();
    }

    @Bean
    @Scope("prototype")
    public AnchorPane clientRegister() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/client/FormClient.fxml" ) );
        fxmlLoader.setControllerFactory ( context::getBean );
        return fxmlLoader.load ( );
    }

    @Bean
    @Scope("prototype")
    public AnchorPane productParent () throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/product/Product.fxml" ) );
        fxmlLoader.setControllerFactory ( context::getBean );
        return fxmlLoader.load ( );
    }
    @Bean
    @Scope("prototype")
    public BorderPane productPurchaseParent() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/popups/ProductPurchase.fxml" ) );
        fxmlLoader.setControllerFactory ( context::getBean );
        return fxmlLoader.load ( );
    }
    @Bean
    @Scope("prototype")
    public AnchorPane purchaseParent () throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/purchase/Purchase.fxml" ) );
        fxmlLoader.setControllerFactory ( context::getBean );
        return fxmlLoader.load ( );
    }

    @Bean
    @Scope("prototype")
    public AnchorPane findProductParent() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/FindProduct.fxml" ) );
        fxmlLoader.setControllerFactory ( context::getBean );
        return fxmlLoader.load ( );
    }

    @Bean
    @Scope("prototype")
    public GridPane productEditParent() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/product/ProductEdit.fxml" ) );
        fxmlLoader.setControllerFactory ( context::getBean );
        return fxmlLoader.load ( );
    }
    @Bean
    @Scope("prototype")
    public AnchorPane productRegisterParent () throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/product/FormProduct.fxml" ) );
        fxmlLoader.setControllerFactory ( context::getBean );
        return fxmlLoader.load ( );
    }

    @Bean
    @Scope("prototype")
    public VBox verificationParent() throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/popups/VerificationCode.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load();
    }
    @Bean
    @Scope("prototype")
    public AnchorPane loginParent () throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/auth/Login.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load ( );
    }
    @Bean
    @Scope("prototype")
    public AnchorPane contactParent () throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/navigation/Contact.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load ( );
    }
    @Bean
    @Scope("prototype")
    public AnchorPane configParent () throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/navigation/Configuration.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load ( );
    }
    @Bean
    @Scope("prototype")
    public VBox developerParent () throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/navigation/Developers.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load ( );
    }
    @Bean
    @Scope("prototype")
    public AnchorPane detailsPurchaseParent () throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/popups/DetailsPurchase.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load ( );
    }
    @Bean
    @Scope("prototype")
    public ScrollPane statisticsParent () throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/navigation/Statistics.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load ( );
    }

    @Bean
    @Scope("prototype")
    public  GridPane purchaseEditParent() throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/purchase/PurchaseEdit.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load ( );
    }

    @Bean
    @Scope("prototype")
    public AnchorPane registerParent () throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/auth/SignUp.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load ( );
    }
    @Bean
    @Scope("prototype")
    public GridPane clientEdit() throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/main/menu/client/ClientEdit.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load ();

    }
    @Bean
    @Scope("prototype")
    public VBox verificationCodePane () throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/popups/VerificationCode.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load ( );
    }

    @Bean
    public Stage stage () {
        return new Stage ( );
    }

    @Bean
    @Scope("prototype")
    public Stage prototypeStage () {
        return new Stage ( );
    }
}
