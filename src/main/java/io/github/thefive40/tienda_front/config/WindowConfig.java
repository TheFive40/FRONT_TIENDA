package io.github.thefive40.tienda_front.config;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
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
    public AnchorPane registerParent () throws IOException {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/auth/SignUp.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader.load ( );
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
