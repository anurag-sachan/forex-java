package com.java;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Email {

    public static void sendEmail(String body) throws FileNotFoundException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        final String fromEmail = "connect2anurags@gmail.com";
        String pathConfigFile="/Users/anurag/Desktop/forex/src/main/java/com/java/config/config.properties";
        try (InputStream input = new FileInputStream(pathConfigFile)) {
            props.load(input);
        }
        final String password = props.getProperty("smtp-code");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            String to = "anuragsachaniitm@gmail.com";
            String subject = "Java Forex Alert";
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setText(body);

            Transport.send(msg);

            System.out.println("Alert sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
