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
        message.setSubject("Підтвердженння реєстрації на сайті L2 Absolute");
        message.setText("Вітаю тебе шукачу пригод!\n\n" +
                "Вітер змін в чудесних світах гри Lineage 2 дує в спину як бувалим ветеранам , так і зеленим новачкам все сильніше і сильніше.\n" +
                "Настав твій час вступити в битву за вічну славу і безмірні багатства. Ось твій код підтвердження реєстрації: " + verificationCode + "\n\n" +
                "Якщо ви не реєструвалися на нашому проєкті,цей лист можна проігнорувати.\n\n" +
                "Успіхів в нових пригодах!");
        // Відправка
        mailSender.send(message);

        return verificationCode; // Повертаємо код для подальшої перевірки
    }
}