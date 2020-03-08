package com.lab.mail_client.helpers;

// TODO: тут надо получить массив объектов Message

import com.lab.mail_client.components.User;

import javax.mail.*;
import java.util.ArrayList;
import java.util.Properties;

public class YandexEmailReceiver
{
    private ArrayList<com.lab.mail_client.components.Message> messages;
    private int nextMessageID;

    public YandexEmailReceiver()
    {
        messages = new ArrayList<>();
        nextMessageID = 0;
    }

    private Properties getServerProperties()
    {
        Properties properties = new Properties();

        // server setting
        properties.put(String.format("mail.%s.host", "imap"), "imap.yandex.ru");
        properties.put(String.format("mail.%s.port", "imap"), "993");

        // SSL setting
        properties.setProperty(
                String.format("mail.%s.socketFactory.class", "imap"),
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(
                String.format("mail.%s.socketFactory.fallback", "imap"),
                "false");
        properties.setProperty(
                String.format("mail.%s.socketFactory.port", "imap"),
                "993");

        return properties;
    }

    public ArrayList<com.lab.mail_client.components.Message> downloadEmails()
    {
        Properties properties = getServerProperties();
        Session session = Session.getDefaultInstance(properties);

        try
        {
            // connects to the message store
            Store store = session.getStore("imap");
            store.connect(User.getEmail(), User.getPassword());

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            // fetches new messages from server
            Message[] messages = folderInbox.getMessages();

            for (int i = 0; i < messages.length; i++)
            {
                Message msg = messages[i];
                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                String subject = msg.getSubject();
                String toList = parseAddresses(msg
                        .getRecipients(Message.RecipientType.TO));
                String ccList = parseAddresses(msg
                        .getRecipients(Message.RecipientType.CC));
                String sentDate = msg.getSentDate().toString();

                String contentType = msg.getContentType();
                String messageContent = "";

                if (contentType.contains("text/plain") || contentType.contains("text/html"))
                {
                    try
                    {
                        Object content = msg.getContent();
                        if (content != null)
                        {
                            messageContent = content.toString();
                        }
                    }
                    catch (Exception ex)
                    {
                        messageContent = "[Error downloading content]";
                        ex.printStackTrace();
                    }
                }

                com.lab.mail_client.components.Message message = new com.lab.mail_client.components.Message();
                message.setId(nextMessageID++);
                message.setSenderEmail(from);
                message.setSubject(subject);
                if (!messageContent.equals(""))
                {
                    message.setText(messageContent);
                }
                else
                {
                    message.setText("There is body, but it is hard to decode a message");
                }
                this.messages.add(message);

                // print out details of each message
                System.out.println("Message #" + (i + 1) + ":");
                System.out.println("\t From: " + from);
                System.out.println("\t To: " + toList);
                System.out.println("\t CC: " + ccList);
                System.out.println("\t Subject: " + subject);
                System.out.println("\t Sent Date: " + sentDate);
                System.out.println("\t Message: " + messageContent);
            }

            // disconnect
            folderInbox.close(false);
            store.close();
        }
        catch (NoSuchProviderException ex)
        {
            System.out.println("No provider for protocol: " + "imap");
            ex.printStackTrace();
        }
        catch (MessagingException ex)
        {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }
        return this.messages;
    }

    /**
     * Returns a list of addresses in String format separated by comma
     *
     * @param address an array of Address objects
     * @return a string represents a list of addresses
     */
    private String parseAddresses(Address[] address)
    {
        StringBuilder listAddress = new StringBuilder();

        if (address != null)
        {
            for (Address value : address)
            {
                listAddress.append(value.toString()).append(", ");
            }
        }
        if (listAddress.length() > 1)
        {
            listAddress = new StringBuilder(listAddress.substring(0, listAddress.length() - 2));
        }

        return listAddress.toString();
    }
}
