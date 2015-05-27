package com.thuongnh.sendingemail;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Zelic on 5/27/15.
 */
public class Mail extends Authenticator {
    String host;
    String fromAddress;
    String password;
    String port;
    String securePort;
    String subject;
    boolean debuggable;
    boolean needAuthenticate;
    String toAddress;
    Multipart multipart;

    /**
     * Constructor
     */
    public Mail() {
        //We will use gmail host in this scenario
        host = "smtp.gmail.com";
        port = "465";
        securePort = "465";
        //You have to provide your real email and password here
        fromAddress = "fromaddress@gmail.com";
        password = "password";
        subject = "Feedback email";
        debuggable = false;
        needAuthenticate = true;

        //Setup multipart and mail cap enables us to attach files.
        multipart = new MimeMultipart();
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }


    /**
     * We use this method to send email
     * @param bodyText
     */
    public void send(String bodyText) {
        //Create properties with mail information
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        //We switch debuggable to TRUE if we want to trace under the hood stuff
        if (debuggable) {
            properties.put("mail.debug", "true");
        }

        if (needAuthenticate) {
            properties.put("mail.smtp.auth", "true");
        }

        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.socketFactory.port", securePort);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");

        //Create a mail session with properties above
        Session session = Session.getInstance(properties, this);

        //Create a mime message to send mail
        MimeMessage message = new MimeMessage(session);
        try {
            //Set from address
            message.setFrom(new InternetAddress(fromAddress));

            //Set to address
            InternetAddress toInetAddress = new InternetAddress(toAddress);
            message.setRecipient(MimeMessage.RecipientType.TO, toInetAddress);

            //Set subject
            message.setSubject(subject);

            //Set sent date
            message.setSentDate(new Date());

            //Set content with multipart
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(bodyText);
            multipart.addBodyPart(bodyPart);
            message.setContent(multipart);

            //Send message with transport
            Transport.send(message);
        } catch (Exception e) {
            Log.e("Mail", e.getMessage(), e);
        }
    }

    /**
     * We have to override this method to authenticate
     * @return
     */
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(fromAddress, password);
    }
}
