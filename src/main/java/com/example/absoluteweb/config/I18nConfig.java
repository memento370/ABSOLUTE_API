package com.example.absoluteweb.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Configuration
public class I18nConfig {

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(new Locale("uk"));  // За замовчуванням українська
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(
                "messages/site/messages",   // Для сайту: messages/site/messages_uk.properties тощо
                "messages/server/messages" // Для сервера
//                "messages/forum/messages"   // Для форуму
        );
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true); // Повертає код, якщо не знайдено (для дебагу)
        messageSource.setFallbackToSystemLocale(false); // Уникає fallback на системну локаль
        return messageSource;
    }
}