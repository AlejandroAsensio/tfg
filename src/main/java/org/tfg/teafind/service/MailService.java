package org.tfg.teafind.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	
	@Autowired
    private JavaMailSender javaMailSender;

    public void enviarEmail(String toEmail, String subject, String body) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setFrom("teafindes@gmail.com");
        helper.setTo(toEmail);
        helper.setText(body, true);
        helper.setSubject(subject);
        javaMailSender.send(mimeMessage);
        System.out.println("Email enviado");
    }
}
