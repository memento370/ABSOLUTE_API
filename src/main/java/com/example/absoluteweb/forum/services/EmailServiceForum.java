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
        message.setSubject("Підтвердження зміни емейлу");
        message.setText("Ваш емейл на форумі l2-absolute.com/forum було вказано,як новий для користувача. Якщо ви не давали на це згоди,цей лист можна проігнорувати. Код : "+verificationCode);
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
    public String sendVerificationChangeLogin(String toEmail) {
        // Генерація 6-значного коду
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        // Створення листа
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Підтвердження зміни логіну на форумі L2 Absolute");
        message.setText("Код підтвердження зміни логіну на формумі : "+verificationCode);
        // Відправка
        mailSender.send(message);

        return verificationCode; // Повертаємо код для подальшої перевірки
    }

    public String sendVerificationChangeOldEmail(String toEmail) {
        // Генерація 6-значного коду
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        // Створення листа
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Підтвердження зміни старого е-мейл на форумі L2 Absolute");
        message.setText("Це код підтвердження відв'язки вашого нинішнього е-мейлу(на який прийшло це повідомлення). Якщо ви не намагалися відв'язати цей е-мейл ,проігноруйте це повідомлення. Ваш кодпідтвердження відв'язки е-мейлу : "+verificationCode);
        // Відправка
        mailSender.send(message);

        return verificationCode; // Повертаємо код для подальшої перевірки
    }
    public String sendVerificationPassword(String toEmail) {
        // Генерація 6-значного коду
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        // Створення листа
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Підтвердження зміни паролю на форумі L2 Absolute");
        message.setText("Це код підтвердження для зміни паролю,якщо ви її не ініціалізували ,цей лист можна проігнорувати. Код : "+verificationCode);
        // Відправка
        mailSender.send(message);

        return verificationCode; // Повертаємо код для подальшої перевірки
    }

}