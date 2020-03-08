package com.lab.mail_client.components;

public class User
{
    private static String email;
    private static String password;
    private static String username;
    private static Message[] messages;

    public User()
    {
    }

    public User(String email, String username, String password)
    {
        User.email = email;
        User.username = username;
        User.password = password;
    }

    public static Message[] getMessages()
    {
        return messages;
    }

    public static void setMessages(Message[] messages)
    {
        User.messages = messages;
    }

    public static String getEmail()
    {
        return email;
    }

    public static void setEmail(String email)
    {
        User.email = email;
    }

    public static String getPassword()
    {
        return password;
    }

    public static void setPassword(String password)
    {
        User.password = password;
    }

    public static String getUsername()
    {
        return username;
    }

    public static void setUsername(String username)
    {
        User.username = username;
    }
}
