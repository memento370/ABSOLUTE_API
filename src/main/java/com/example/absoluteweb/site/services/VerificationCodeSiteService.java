package com.example.absoluteweb.site.services;



import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeSiteService {
    // Зберігаємо код у кеші на 5 хв
    @CachePut(value = "verificationCodes", key = "#email")
    public String saveCode(String email, String code) {
        return code;
    }

    // Отримуємо код з кешу
    @Cacheable(value = "verificationCodes", key = "#email")
    public String getCode(String email) {
        return null; // Spring замінює це значення кешем
    }

    // Видаляємо код після успішної перевірки
    @CacheEvict(value = "verificationCodes", key = "#email")
    public void deleteCode(String email) {
    }
}
