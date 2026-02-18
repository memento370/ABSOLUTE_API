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
    public void deleteCode(String email) {  }

    // ====== Зміна email (окремий кеш, щоб не було колізій) ======
    @CachePut(value = "emailChangeCodes", key = "#oldEmail")
    public String saveEmailChangeCode(String oldEmail, String code) {
        return code;
    }

    @Cacheable(value = "emailChangeCodes", key = "#oldEmail", unless = "#result == null")
    public String getEmailChangeCode(String oldEmail) {
        return null;
    }

    @CacheEvict(value = "emailChangeCodes", key = "#oldEmail")
    public void deleteEmailChangeCode(String oldEmail) { }

    // ====== Login change ======
    @CachePut(value = "loginChangeCodes", key = "#email")
    public String saveLoginChangeCode(String email, String code) { return code; }

    @Cacheable(value = "loginChangeCodes", key = "#email", unless = "#result == null")
    public String getLoginChangeCode(String email) { return null; }

    @CacheEvict(value = "loginChangeCodes", key = "#email")
    public void deleteLoginChangeCode(String email) { }


    // ====== Password change ======
    @CachePut(value = "passwordChangeCodes", key = "#email")
    public String savePasswordChangeCode(String email, String code) { return code; }

    @Cacheable(value = "passwordChangeCodes", key = "#email", unless = "#result == null")
    public String getPasswordChangeCode(String email) { return null; }

    @CacheEvict(value = "passwordChangeCodes", key = "#email")
    public void deletePasswordChangeCode(String email) { }

    @CachePut(value = "passwordResetCodes", key = "#email")
    public String savePasswordResetCode(String email, String code) {
        return code;
    }

    @Cacheable(value = "passwordResetCodes", key = "#email")
    public String getPasswordResetCode(String email) {
        return null;
    }

    @CacheEvict(value = "passwordResetCodes", key = "#email")
    public void deletePasswordResetCode(String email) {}

}
