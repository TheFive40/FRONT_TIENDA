package io.github.thefive40.tienda_front.service;
import javafx.concurrent.Task;
import lombok.Getter;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
/**
 * EmailService is a service class responsible for sending verification codes via email.
 * It uses the JavaMail API to handle email communication and generates random
 * verification codes.
 */
@Service
@Getter
public class EmailService {
    /**
     * Stores the latest verification code sent.
     */
    private String code;

    /**
     * Sends a verification code to the specified recipient email address.
     * This method runs the email sending operation in a background thread using JavaFX's {@link Task}.
     *
     * @param recipient the recipient's email address.
     * @param code      the verification code to be sent.
     */
    protected void sendVerificationCode ( String recipient, String code ) {
        Task<Void> task = new Task<Void> ( ) {
            @Override
            protected Void call () throws Exception {
                String from = "boomajeanfranco@gmail.com";
                String host = "smtp.gmail.com";
                Properties properties = System.getProperties ( );
                properties.put ( "mail.smtp.host", host );
                properties.put ( "mail.smtp.port", "465" );
                properties.put ( "mail.smtp.ssl.enable", "true" );
                properties.put ( "mail.smtp.auth", "true" );
                properties.put ( "mail.debug", "true" );
                Session session = Session.getInstance ( properties, new Authenticator ( ) {
                    protected PasswordAuthentication getPasswordAuthentication () {
                        return new PasswordAuthentication ( from, "bkbp rpqu ssaf rnlz" );
                    }
                } );
                MimeMessage message = new MimeMessage ( session );
                try {
                    message.setFrom ( new InternetAddress ( from ) );
                    message.addRecipient ( Message.RecipientType.TO, new InternetAddress ( recipient ) );
                    message.setSubject ( "Verification Code" );
                    message.setText ( "Your verification code is: " + code );
                    Transport.send ( message );
                } catch (MessagingException e) {
                    throw new RuntimeException ( e );
                }
                return null;
            }
        };
        new Thread(task).start();
    }
    /**
     * Generates a random verification code and sends it to the provided email address.
     *
     * @param email the recipient's email address.
     * @throws MessagingException if there is an error while sending the email.
     */
    public void sendVerificationCode ( String email ) throws MessagingException {
        String verificationCode = String.valueOf ( (int) (Math.random ( ) * 900000) + 100000 );
        code = verificationCode;
        sendVerificationCode ( email, verificationCode );
    }

}
