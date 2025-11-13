package com.example.absoluteweb.site.controllers;

import com.example.absoluteweb.site.DTO.SiteRegistrationRequest;
import com.example.absoluteweb.site.DTO.SiteVerificationRequest;
import com.example.absoluteweb.site.exceptions.AccountExceptions;
import com.example.absoluteweb.site.services.EmailServiceSite;
import com.example.absoluteweb.site.services.SiteAccountsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("api/site/accounts")
public class SiteAccountsController {

    @Autowired
    private SiteAccountsService siteAccountsService;
    @Autowired
    private EmailServiceSite emailService;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    public SiteAccountsController(SiteAccountsService siteAccountsService) {
        this.siteAccountsService = siteAccountsService;
    }

    @PostMapping("/check-register")
    private ResponseEntity checkRegister(@Valid @RequestBody SiteRegistrationRequest regAcc) {
        try {
            return siteAccountsService.checkRegister(regAcc);
        }
        catch (AccountExceptions e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> request) {
        Locale locale = LocaleContextHolder.getLocale();
        String email = request.get("email");
        try {
            siteAccountsService.sendVerificationCode(email);
            return ResponseEntity.ok(messageSource.getMessage("site.send.verification.code", null, locale));
        } catch (MailSendException e) {
            return ResponseEntity.badRequest().body(
                    messageSource.getMessage("site.send.verification.code.invalid.email", null, locale)
            );
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@Valid @RequestBody SiteVerificationRequest request) {
        return siteAccountsService.verifyCodeAndRegister(request);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody SiteRegistrationRequest login) {
        try{
            return siteAccountsService.login(login);
        } catch (AccountExceptions e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
