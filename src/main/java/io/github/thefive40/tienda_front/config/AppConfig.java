package io.github.thefive40.tienda_front.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thefive40.tienda_front.TiendaFrontApplication;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AppConfig {
    private final ApplicationContext context;

    public AppConfig ( ApplicationContext context ) {
        this.context = context;
    }

    @Bean
    @Scope("prototype")
    public FXMLLoader loginFXML () {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/auth/Login.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader;
    }
    @Bean
    @Scope("prototype")
    public FXMLLoader signUpFXML () {
        FXMLLoader loader = new FXMLLoader ( AppConfig.class.getResource ( "/templates/auth/SignUp.fxml" ) );
        loader.setControllerFactory ( context::getBean );
        return loader;
    }

    @Bean
    public Logger logger (){
        return LoggerFactory.getLogger ( TiendaFrontApplication.class );
    }
    @Bean
    public ObjectMapper mapper (){
        return new ObjectMapper (  );
    }



}
