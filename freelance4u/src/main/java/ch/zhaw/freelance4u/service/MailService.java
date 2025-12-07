package ch.zhaw.freelance4u.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ch.zhaw.freelance4u.model.Mail;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getTo());
        message.setSubject(mail.getSubject());
        message.setText(mail.getMessage());

        mailSender.send(message);
    }
}
