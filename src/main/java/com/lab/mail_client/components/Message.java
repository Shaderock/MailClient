package com.lab.mail_client.components;

public class Message
{
    private int id;
    private String senderEmail;
    private String subject;
    private String text;

    public Message()
    {
    }

    public Message(int id, String senderEmail, String subject, String text)
    {
        this.id = id;
        this.senderEmail = senderEmail;
        this.subject = subject;
        this.text = text;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getSenderEmail()
    {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail)
    {
        this.senderEmail = senderEmail;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }
}
