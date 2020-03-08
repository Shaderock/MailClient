package com.lab.mail_client.helpers;

import com.lab.mail_client.components.User;
import com.sun.mail.smtp.SMTPTransport;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class YandexEmailSender
{
    public void send(String recipientEmail, String subject, String body)
    {
        Properties prop = new Properties();
        prop.put("mail.smtps.auth", "true");
        prop.put("mail.smtps.starttls.enable", "true");
        prop.put("mail.smtps.host", "smtp.yandex.ru");
        prop.put("mail.smtps.ssl.trust", "smtp.yandex.ru");
        prop.put("mail.smtps.connectiontimeout", "5000");
        prop.put("mail.smtps.timeout", "5000");

        Session session = Session.getInstance(prop, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(User.getUsername(), User.getPassword());
            }
        });

        Message message = new MimeMessage(session);
        try
        {
        message.setFrom(new InternetAddress(User.getEmail()));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(body, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);
        SMTPTransport t =
                (SMTPTransport) session.getTransport("smtps");
        t.connect("smtp.yandex.ru", User.getEmail(), User.getPassword());
        t.sendMessage(message, message.getAllRecipients());
        System.out.println("Response: " + t.getLastServerResponse());
            t.close();
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }
}
