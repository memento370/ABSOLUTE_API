package com.example.absoluteweb.site.controllers;

import com.example.absoluteweb.site.DTO.SiteRegistrationRequest;
import com.example.absoluteweb.site.exceptions.AccountExceptions;
import com.example.absoluteweb.site.services.EmailServiceSite;
import com.example.absoluteweb.site.services.SiteAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/site/accounts")
public class SiteAccountsController {

    @Autowired
    private SiteAccountsService siteAccountsService;
    @Autowired
    private EmailServiceSite emailService;

    private final Map<String, String> verificationCodes = new HashMap<>();

    @Autowired
    public SiteAccountsController(SiteAccountsService siteAccountsService) {
        this.siteAccountsService = siteAccountsService;
    }

    @PostMapping("/register")
    private ResponseEntity createAccount(@RequestBody SiteRegistrationRequest regAcc) {
        try {
            siteAccountsService.createAccaunt(regAcc);
            return ResponseEntity.ok("createUser its ok");
        }
        catch (AccountExceptions e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/check-register")
    private ResponseEntity checkRegister(@RequestBody SiteRegistrationRequest regAcc) {
        try {
            siteAccountsService.checkRegister(regAcc);
            return ResponseEntity.ok("login and e-mail valid");
        }
        catch (AccountExceptions e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try{
            String code = emailService.sendVerificationEmail(email);
            verificationCodes.put(email, code);
            return ResponseEntity.ok("Код підтвердження відправлено на " + email);
        }catch (MailSendException e){
            return ResponseEntity.badRequest().body("Введён некоректный e-mail");
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        if (verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code)) {
            verificationCodes.remove(email);
            return ResponseEntity.ok("Код подтверждён");
        } else {
            return ResponseEntity.badRequest().body("Введен неверный код.");
        }
    }
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody SiteRegistrationRequest login) {
        try{
            return siteAccountsService.login(login);
        } catch (AccountExceptions e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
