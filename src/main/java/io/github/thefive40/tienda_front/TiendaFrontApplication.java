package io.github.thefive40.tienda_front;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;

@SpringBootApplication
@Async
public class TiendaFrontApplication {

	public static void main(String[] args) {
		Application.launch ( ApplicationFX.class ,args);
	}


}
