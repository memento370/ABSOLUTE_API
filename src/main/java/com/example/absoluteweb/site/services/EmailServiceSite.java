package com.example.absoluteweb.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceSite {

    @Autowired
    private JavaMailSender mailSender;

    public String sendVerificationEmail(String toEmail) {
        // Генерація 6-значного коду
        String verificationCode = String.format("%06d", new Random().nextInt(999999));

        // Створення листа
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Подтверждение регистрации L2 Absolute");
        message.setText("Приветствую тебя, путник!\n\n" +
                "Ветер перемен в чудесных мирах игры Lineage 2 дует в спину как бывалым игрокам, так и зеленым новичкам с каждым днём всё сильнее.\n" +
                "Пришел твой черед вступить в великую битву за вечную славу и несметные богатства. Вот твой код подтверждения регистрации: " + verificationCode + "\n\n" +
                "Если вы не регистрировались на нашем проекте, это письмо можно просто проигнорировать.\n\n" +
                "Удачи тебе в новых странствиях!");
        // Відправка
        mailSender.send(message);

        return verificationCode; // Повертаємо код для подальшої перевірки
    }
}