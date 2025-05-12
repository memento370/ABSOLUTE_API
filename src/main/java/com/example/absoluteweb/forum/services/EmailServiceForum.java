package com.example.absoluteweb.forum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceForum {

    @Autowired
    private JavaMailSender mailSender;

    public String sendVerificationEmail(String toEmail) {
        // Генерація 6-значного коду
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        // Створення листа
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Подтверждение регистрации на форуме L2 Absolute");
        message.setText("Код подтверждения регистрации на форуме L2 Absolute : "+verificationCode);
        // Відправка
        mailSender.send(message);

        return verificationCode; // Повертаємо код для подальшої перевірки
    }

    public String sendVerificationEmailRestore(String toEmail) {
        // Генерація 6-значного коду
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        // Створення листа
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Подтверждение востановления пароля на форуме L2 Absolute");
        message.setText("Код подтверждения востановления пароля на форуме L2 Absolute : "+verificationCode);
        // Відправка
        mailSender.send(message);

        return verificationCode; // Повертаємо код для подальшої перевірки
    }
}