package codesake.in.securecapita.service;

import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;

public interface EmailService {
    void sendSimpleEmailMessage(String name,String to, String token) throws CatchGlobalException;
    void sendMimeEmailMessageWithAttachments(String name,String to, String token);
    void sendMimeEmailMessageWithEmbedImages(String name,String to, String token);
    void sendMimeEmailMessageWithEmbedFiles(String name,String to, String token);
    void sendHtmlEmail(String name,String to, String token);
    void sendHtmlEmailEmbedFiles(String name,String to, String token);
}
