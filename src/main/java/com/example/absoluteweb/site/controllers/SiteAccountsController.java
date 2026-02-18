package com.example.absoluteweb.site.controllers;

import com.example.absoluteweb.site.DTO.*;
import com.example.absoluteweb.site.exceptions.AccountExceptions;
import com.example.absoluteweb.site.principals.AccountPrincipal;
import com.example.absoluteweb.site.services.EmailServiceSite;
import com.example.absoluteweb.site.services.SiteAccountsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private ResponseEntity checkRegister(
            @Valid @RequestBody SiteRegistrationRequest regAcc,BindingResult br) {
        if (br.hasErrors()) {
            String msg = br.getFieldErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(msg);
        }
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
    public ResponseEntity login(
            @Valid @RequestBody SiteRegistrationRequest login,BindingResult br) {
        if (br.hasErrors()) {
            String msg = br.getFieldErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(msg);
        }
        try{
            return siteAccountsService.login(login);
        } catch (AccountExceptions e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/email-change/request")
    public ResponseEntity<?> requestEmailChangeCode(@AuthenticationPrincipal AccountPrincipal principal) {
        try {
            return siteAccountsService.requestEmailChangeCode(principal);
        } catch (AccountExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/email-change/confirm")
    public ResponseEntity<?> confirmEmailChange(
            @AuthenticationPrincipal AccountPrincipal principal,
            @Valid @RequestBody EmailChangeConfirmDTO dto,
            BindingResult br
    ) {
        if (br.hasErrors()) {
            // після зміни DTO на "{key}" тут буде вже локалізований текст
            String msg = br.getFieldErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(msg);
        }
        try {
            return siteAccountsService.confirmEmailChange(principal, dto);
        } catch (AccountExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login/remind")
    public ResponseEntity<?> remindLogin(@AuthenticationPrincipal AccountPrincipal principal) {
        try {
            return siteAccountsService.remindLogin(principal);
        } catch (AccountExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/password-change/request")
    public ResponseEntity<?> requestPasswordChangeCode(@AuthenticationPrincipal AccountPrincipal principal) {
        try {
            return siteAccountsService.requestPasswordChangeCode(principal);
        } catch (AccountExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/password-change/confirm")
    public ResponseEntity<?> confirmPasswordChange(
            @AuthenticationPrincipal AccountPrincipal principal,
            @Valid @RequestBody PasswordChangeConfirmDTO dto,
            BindingResult br
    ) {
        if (br.hasErrors()) {
            String msg = br.getFieldErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(msg);
        }
        try {
            return siteAccountsService.confirmPasswordChange(principal, dto);
        } catch (AccountExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<?> requestPasswordReset(
            @Valid @RequestBody PasswordResetRequestDTO dto,
            BindingResult br
    ) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(br.getFieldErrors().get(0).getDefaultMessage());
        }
        try {
            return siteAccountsService.requestPasswordReset(dto);
        } catch (AccountExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<?> confirmPasswordReset(
            @Valid @RequestBody PasswordResetConfirmDTO dto,
            BindingResult br
    ) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(br.getFieldErrors().get(0).getDefaultMessage());
        }
        try {
            return siteAccountsService.confirmPasswordReset(dto);
        } catch (AccountExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
