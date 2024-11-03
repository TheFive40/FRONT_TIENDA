package io.github.thefive40.tienda_front.service;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import javafx.concurrent.Task;
import org.springframework.stereotype.Service;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class UserRegistrationService {

    private AuthService service;

    public UserRegistrationService ( AuthService service ) {
        this.service = service;
    }

    /**
     * Starts the registration task in a separate thread.
     * <p>
     * This method creates a new {@link Task} that repeatedly checks if the
     * {@link AuthService} is committed to proceeding with the registration.
     * Once the service confirms that it is committed, it registers a new user
     * using the provided details from the input fields, encapsulated in a
     * {@link ClientDTO}. The task runs in the background to avoid blocking
     * the JavaFX Application Thread, ensuring a responsive user interface.
     */
    public void registerUser ( ClientDTO clientDTO ) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor ( );
        scheduler.scheduleAtFixedRate ( () -> {
            if (service.isCommit ( )) {
                clientDTO.setUrl ( "/static/media/images/util/profile.jpeg" );
                service.register ( clientDTO );
                scheduler.shutdown ( );
            }
        }, 0, 50, TimeUnit.MILLISECONDS );
    }

}
