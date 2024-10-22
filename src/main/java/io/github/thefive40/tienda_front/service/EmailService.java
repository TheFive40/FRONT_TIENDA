package io.github.thefive40.tienda_front.service;
import javafx.concurrent.Task;
import lombok.Getter;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@Getter
public class EmailService {
    private String code;
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

    public void sendVerificationCode ( String email ) throws MessagingException {
        String verificationCode = String.valueOf ( (int) (Math.random ( ) * 900000) + 100000 );
        code = verificationCode;
        sendVerificationCode ( email, verificationCode );
    }

}
