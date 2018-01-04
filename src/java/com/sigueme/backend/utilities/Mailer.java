
package com.sigueme.backend.utilities;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mailer {

    public static void send(String para, String sujeto, String mensaje) throws UnsupportedEncodingException {

        final String user = "sistema.sigueme@gmail.com";//cambiará en consecuencia al servidor utilizado
        final String pass = "S1guEMe123";

//1st paso) Obtener el objeto de sesión
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com"); // envia 
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "25");
        props.setProperty("mail.smtp.starttls.required", "false");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

//2nd paso)compose message
        try {

            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource("i:/cartagena.txt")));
            adjunto.setFileName("cartagena.txt");

            BodyPart texto = new MimeBodyPart();
            texto.setText(mensaje);
            MimeMultipart multiparte = new MimeMultipart();
            multiparte.addBodyPart(texto);
            multiparte.addBodyPart(adjunto);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user, "Nelson Rodriguez"));
            InternetAddress[] destinatarios = {
                new InternetAddress(para),
                //    new InternetAddress("nelrodrigueza@misena.edu.co"),
                //    new InternetAddress("kelyen2039@gmail.com"),
                new InternetAddress("dsrobayo80@misena.edu.co")
            };

            message.setRecipients(Message.RecipientType.TO, destinatarios);
            message.setSubject(sujeto);
            message.setContent(multiparte, "text/html; charset=utf-8");

            //3rd paso)send message
            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
