package com.inn.cafe.utils;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

@Service
public class EmailUtils {
    @Autowired
    private JavaMailSender emailsender;

    public void sendsimpleMessage(String to, String subject , String text , List<String > list){
        SimpleMailMessage message=new SimpleMailMessage(); // creates a Simple Mail Message.
                                                          // It is a simple representation
                                                          // of an email message that supports only text
                                                          // and simple header fields.

        message.setFrom("info.infofull@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if(list !=null && list.size()>0)
             message.setCc(getCcArray(list));
        emailsender.send(message);

    }

    private String[] getCcArray(List<String> ccList){
        String [] cc=new String[ccList.size()];
        for(int i=0 ;i<ccList.size();i++){
            cc[i]=ccList.get(i);
        }
        return cc;
    }

    public void forgotMail(String to ,String subject , String password) throws MessagingException, jakarta.mail.MessagingException {

        MimeMessage message=emailsender.createMimeMessage(); //creates a MIME (Multipurpose Internet Mail Extensions) message.
                                                             // It is an object representation of an email
                                                            //message that supports text, HTML, attachments,
                                                            //and non-ASCII characters.
            //In this line of code, the MimeMessageHelper is being constructed with two arguments: message and true.
            // message is an instance of MimeMessage, and the true argument indicates that the MimeMessageHelper
            // is being constructed in "multipart"
            // mode, which means that it supports multiple parts such as text, HTML, and attachments within a single
            // message.
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom("info.infofull@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            String htmlMsg="<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
            message.setContent(htmlMsg,"text/html");
            emailsender.send(message);


    }
}
