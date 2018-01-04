/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.utilities;

/**
 *
 * @author Santi
 */
import com.sigueme.backend.entities.User;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public final class Mail {

    public static void send(List<User> para, String sujeto, String mensaje) throws UnsupportedEncodingException {
        final String user = "sistema.sigueme@gmail.com";//Correo del cual vamos a enviar el emil
        final String pass = "S1guEMe123";//Clave del email anterior
        System.out.println("va a enviar correo");
        //1st paso) Obtener el objeto de sesión
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com"); 
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.starttls.required", "false");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });
        System.out.println("2 paso");
        //2nd paso)compose message
        try {
            /*BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource("C:\\Users\\Microsoft Windows 10\\Desktop\\File\\Archivo.txt")));
            adjunto.setFileName("Archivo.txt");
             */
            BodyPart texto = new MimeBodyPart();
            texto.setText(mensaje);
            MimeMultipart multiparte = new MimeMultipart();
            multiparte.addBodyPart(texto);
            //multiparte.addBodyPart(adjunto);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user, "Sigueme"));
            //retornarDestinatarios(para);

            InternetAddress[] destinatarios = new InternetAddress[para.size()];

            for (int i = 0; i < para.size(); i++) {
                destinatarios[i] = new InternetAddress(para.get(i).getEmail());
            }

            message.addRecipients(Message.RecipientType.TO, destinatarios);
            message.setSubject(sujeto);
            message.setContent(multiparte, "text/html; charset=utf-8");
            System.out.println("antes");
            //3rd paso)send message
            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
//            throw new RuntimeException(e);
System.out.println("errrrp" + e.getMessage());
        }

    }
}
