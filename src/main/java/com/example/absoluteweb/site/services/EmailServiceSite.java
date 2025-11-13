package com.example.absoluteweb.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

@Service
public class EmailServiceSite {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MessageSource messageSource;

    public String sendVerificationEmail(String toEmail) {
        // Генерація 6-значного коду
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        Locale locale = LocaleContextHolder.getLocale();

        // Створення листа
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(messageSource.getMessage("site.email.send.confirm.01", null, locale));
        message.setText(messageSource.getMessage("site.email.send.confirm.02", null, locale) + verificationCode + "\n\n" +
                messageSource.getMessage("site.email.send.confirm.03", null, locale) +
                messageSource.getMessage("site.email.send.confirm.04", null, locale));
        // Відправка
        mailSender.send(message);
        return verificationCode; // Повертаємо код для подальшої перевірки
    }
}