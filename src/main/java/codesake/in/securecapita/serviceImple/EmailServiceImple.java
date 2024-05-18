package codesake.in.securecapita.serviceImple;

import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImple implements EmailService {

    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "emailtemplate";
    public static final String TEXT_HTML_ENCONDING = "text/html";

    @Value("${spring.mail.verify.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender javaMailSender;


    @Override
    public void sendSimpleEmailMessage(String name, String to, String token) throws CatchGlobalException {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setText("Hey, I am working fine");
            javaMailSender.send(simpleMailMessage);
        }
        catch (Exception ex){
            throw new CatchGlobalException("fromEmail: "+fromEmail+" "+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    @Override
    public void sendMimeEmailMessageWithAttachments(String name, String to, String token) {

    }

    @Override
    public void sendMimeEmailMessageWithEmbedImages(String name, String to, String token) {

    }

    @Override
    public void sendMimeEmailMessageWithEmbedFiles(String name, String to, String token) {

    }

    @Override
    public void sendHtmlEmail(String name, String to, String token) {

    }

    @Override
    public void sendHtmlEmailEmbedFiles(String name, String to, String token) {

    }
}
