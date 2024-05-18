package codesake.in.securecapita.serviceImple;

import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.service.EmailService;
import codesake.in.securecapita.utils.EmailUtils;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Map;
import java.util.Objects;

import static codesake.in.securecapita.utils.EmailUtils.getVerificationUrl;

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

    private final TemplateEngine templateEngine;


    @Override
    @Async
    public void sendSimpleEmailMessage(String name, String to, String token) throws CatchGlobalException {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setText(EmailUtils.getEmailMessage(name,host,token));
            javaMailSender.send(simpleMailMessage);
        }
        catch (Exception ex){
            throw new CatchGlobalException("fromEmail: "+fromEmail+" "+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    @Override
    @Async
    public void sendMimeEmailMessageWithAttachments(String name, String to, String token) throws CatchGlobalException {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(EmailUtils.getEmailMessage(name,host,token));

            log.info("System.getProperty(\"user.home\") : now we are adding attachements => "+System.getProperty("user.home") );
            //Add Attachements
            FileSystemResource luffy = new FileSystemResource(new
                    File(System.getProperty("user.home") + "/Downloads/luffy-one-piece-4k.jpg"));

            helper.addAttachment(Objects.requireNonNull(luffy.getFilename()),luffy);


            javaMailSender.send(message);
        }
        catch (Exception ex){
            log.info("sendMimeEmailMessageWithAttachments: "+ex);
            throw new CatchGlobalException("fromEmail: "+fromEmail+" "+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }



    @Override
    @Async
    public void sendMimeEmailMessageWithEmbedFiles(String name, String to, String token) throws CatchGlobalException {

        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(EmailUtils.getEmailMessage(name,host,token));

            log.info("System.getProperty(\"user.home\") : now we are adding attachements => "+System.getProperty("user.home") );
            //Add Attachements
            FileSystemResource luffy = new FileSystemResource(new
                    File(System.getProperty("user.home") + "/Downloads/luffy-one-piece-4k.jpg"));

            helper.addInline(getContentId(Objects.requireNonNull(luffy.getFilename())),luffy);


            javaMailSender.send(message);
        }
        catch (Exception ex){
            log.info("sendMimeEmailMessageWithAttachments: "+ex);
            throw new CatchGlobalException("fromEmail: "+fromEmail+" "+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }


    }


    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) throws CatchGlobalException {

        try {
            Context context = new Context();
            /*context.setVariable("name", name);
            context.setVariable("url", getVerificationUrl(host, token));*/
            context.setVariables(Map.of("name", name, "url", getVerificationUrl(host, token)));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new CatchGlobalException(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.PROCESSING.value());
        }

    }

    @Override
    @Async
    public void sendHtmlEmailEmbedFiles(String name, String to, String token) throws CatchGlobalException {

        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            //helper.setText(text, true);
            Context context = new Context();
            context.setVariables(Map.of("name", name, "url", getVerificationUrl(host, token)));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            // Add HTML email body
            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, TEXT_HTML_ENCONDING);
            mimeMultipart.addBodyPart(messageBodyPart);

            // Add images to the email body
            BodyPart imageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(System.getProperty("user.home") + "/Downloads/luffy-one-piece-4k.jpg");
            imageBodyPart.setDataHandler(new DataHandler(dataSource));
            imageBodyPart.setHeader("Content-ID", "image");
            mimeMultipart.addBodyPart(imageBodyPart);

            javaMailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new CatchGlobalException(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.PROCESSING.value());

        }

    }

    private MimeMessage getMimeMessage() {
        return javaMailSender.createMimeMessage();
    }

    private String getContentId(String filename) {
        return "<"+filename+">";
    }
}
