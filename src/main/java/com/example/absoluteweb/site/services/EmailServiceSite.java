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

    public String sendEmailChangeVerification(String toEmail) {
        String codeСhangeEmail = String.format("%06d", new Random().nextInt(999999));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Підтвердження зміни e-mail");
        message.setText(
                "Ви запросили зміну e-mail на сайті L2 Absolute.\n\n" +
                        "Код підтвердження: " + codeСhangeEmail + "\n\n" +
                        "Якщо це були не ви — просто ігноруйте цей лист."
        );

        mailSender.send(message);
        return codeСhangeEmail;
    }

    public void sendLoginReminder(String toEmail, String login) {
        Locale locale = LocaleContextHolder.getLocale();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(messageSource.getMessage("site.email.login.remind.subject", null, locale));

        String body =
                messageSource.getMessage("site.email.login.remind.body.prefix", null, locale)
                        + login
                        + "\n\n"
                        + messageSource.getMessage("site.email.send.confirm.03", null, locale)
                        + messageSource.getMessage("site.email.send.confirm.04", null, locale);

        message.setText(body);

        mailSender.send(message);
    }

    public String sendPasswordChangeVerification(String toEmail) {
        // Генерація 6-значного коду (окремо для зміни паролю)
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        Locale locale = LocaleContextHolder.getLocale();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(messageSource.getMessage("site.email.password.change.subject", null, locale));
        message.setText(
                messageSource.getMessage("site.email.password.change.body.prefix", null, locale)
                        + verificationCode
                        + "\n\n"
                        + messageSource.getMessage("site.email.send.confirm.03", null, locale)
                        + messageSource.getMessage("site.email.send.confirm.04", null, locale)
        );

        mailSender.send(message);
        return verificationCode;
    }

    public String sendPasswordResetEmail(String toEmail, String login) {
        String code = String.format("%06d", new Random().nextInt(999999));
        Locale locale = LocaleContextHolder.getLocale();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(messageSource.getMessage("site.email.password.reset.subject", null, locale));

        String body =
                messageSource.getMessage("site.email.password.reset.body.login", new Object[]{login}, locale)
                        + "\n"
                        + messageSource.getMessage("site.email.password.reset.body.code", new Object[]{code}, locale)
                        + "\n\n"
                        + messageSource.getMessage("site.email.send.confirm.03", null, locale)
                        + messageSource.getMessage("site.email.send.confirm.04", null, locale);

        message.setText(body);

        mailSender.send(message);
        return code;
    }
}