package com.lab.mail_client.controller;

import com.lab.mail_client.components.Message;
import com.lab.mail_client.components.User;
import com.lab.mail_client.helpers.YandexEmailReceiver;
import com.lab.mail_client.helpers.YandexEmailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class AppController
{
    @GetMapping("/")
    public String setLoginPage()
    {
        return "login";
    }

    @PostMapping("/")
    public String getCredentials()
    {
        return "send";
    }

    public Message[] arrayListToMessages(ArrayList<Message> messagesList)
    {
        return messagesList.toArray(new Message[0]);
    }

    @GetMapping("/get")
    public String setReceiverPage(Model model)
    {
        YandexEmailReceiver yandexEmailReceiver = new YandexEmailReceiver();
        ArrayList<Message> messagesList = yandexEmailReceiver.downloadEmails();
        Message[] messages = arrayListToMessages(messagesList);
        User.setMessages(messages);
        model.addAttribute("messages", messages);
        return "get";
    }

    @PostMapping("/get")
    public String showMessage()
    {
        return "message";
    }

    @GetMapping("/send")
    public String setSenderPage(
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password)
    {
        User.setEmail(email);
        User.setUsername(username);
        User.setPassword(password);
        System.out.println(email);
        System.out.println(password);
        return "send";
    }

    @PostMapping("/send")
    public void sendEmail(
            @RequestParam(name = "recipientEmail", required = false) String recipientEmail,
            @RequestParam(name = "subject", required = false) String subject,
            @RequestParam(name = "body", required = false) String body
    )
    {
        YandexEmailSender yandexEmailSender = new YandexEmailSender();
        System.out.println(recipientEmail + " " + subject + " " + body);
        yandexEmailSender.send(recipientEmail, subject, body);
    }

    @GetMapping("/message")
    public String setMessage(@RequestParam int id, Model model)
    {
        Message[] messages = User.getMessages();
        Message message = messages[id];
        model.addAttribute("message", message);
        return "message";
    }
}
