package com.example.absoluteweb.site.services;

import com.example.absoluteweb.config.JwtUtils;
import com.example.absoluteweb.server.DTO.ServerRegistrationRequest;
import com.example.absoluteweb.server.services.ServerAccountsService;
import com.example.absoluteweb.site.DTO.SiteRegistrationRequest;
import com.example.absoluteweb.site.DTO.SiteVerificationRequest;
import com.example.absoluteweb.site.entity.SiteAccounts;
import com.example.absoluteweb.site.exceptions.AccountExceptions;
import com.example.absoluteweb.site.repository.SiteAccountsRep;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Security;
import java.util.*;

@Service
public class SiteAccountsService {

    private final SiteAccountsRep siteAccountsRep;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private Validator validator;  // Для валідації ентіті
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private EmailServiceSite emailService;
    @Autowired
    private VerificationCodeSiteService verificationCodeService;
    @Autowired
    private ServerAccountsService serverAccountsService;

    @Autowired
    public SiteAccountsService(SiteAccountsRep siteAccountsRep, PasswordEncoder passwordEncoder) {
        this.siteAccountsRep = siteAccountsRep;
        this.passwordEncoder = passwordEncoder;
    }
    public ResponseEntity<?> createAccaunt(SiteRegistrationRequest regAcc) throws AccountExceptions {
        Locale locale = LocaleContextHolder.getLocale();
        SiteAccounts acc = new SiteAccounts();
        acc.setL2email(regAcc.getL2email());
        acc.setLogin(regAcc.getLogin());
        acc.setRole("USER");
        acc.setPassword(passwordEncoder.encode(regAcc.getPassword()));
        if (siteAccountsRep.findByLogin(regAcc.getLogin()) != null || siteAccountsRep.findByL2email(regAcc.getL2email()) != null) {
            throw new AccountExceptions(messageSource.getMessage("site.account.exists", null, locale));
        }
        // Валідація ентіті
        Set<ConstraintViolation<SiteAccounts>> violations = validator.validate(acc);
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<SiteAccounts> violation : violations) {
                errors.append(messageSource.getMessage(violation.getMessage(), null, locale)).append("; ");
            }
            throw new AccountExceptions(errors.toString());
        }
        try {
            siteAccountsRep.save(acc);
            return ResponseEntity.ok(messageSource.getMessage("site.create.success", null, locale));
        } catch (Exception ex) {
            throw new AccountExceptions(messageSource.getMessage("site.save.error", null, locale));
        }
    }


    public ResponseEntity login(SiteRegistrationRequest login) throws AccountExceptions {
        SiteAccounts user = siteAccountsRep.findByLogin(login.getLogin());
        Locale locale = LocaleContextHolder.getLocale();
        if (user == null) {
            throw new AccountExceptions(messageSource.getMessage("site.login.notfound", null, locale));
        }
        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new AccountExceptions(messageSource.getMessage("site.login.password.incorrect", null, locale));
        }
        String token = jwtUtils.generateToken(user.getLogin(), user.getRole());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole());
        response.put("login", user.getLogin());
        response.put("message", messageSource.getMessage("site.login.success", null, locale));

        return ResponseEntity.ok(response);
    }

    // Надсилання та кешування коду
    public String sendVerificationCode(String email) {
        String code = emailService.sendVerificationEmail(email);
        verificationCodeService.saveCode(email, code);
        return code;
    }

    public ResponseEntity<String> verifyCodeAndRegister(SiteVerificationRequest request) {
        Locale locale = LocaleContextHolder.getLocale();

        String email = request.getL2email();
        String code = request.getCode();
        String cachedCode = verificationCodeService.getCode(email);

        if (cachedCode != null && cachedCode.equals(code)) {
            verificationCodeService.deleteCode(email);

            try {
                SiteRegistrationRequest regReq = new SiteRegistrationRequest();
                regReq.setLogin(request.getLogin());
                regReq.setL2email(request.getL2email());
                regReq.setPassword(request.getPassword());
                createAccaunt(regReq);

                SiteRegistrationRequest serverRegistrationRequest = new SiteRegistrationRequest();
                serverRegistrationRequest.setLogin(request.getLogin());
                serverRegistrationRequest.setL2email(request.getL2email());
                serverRegistrationRequest.setPassword(request.getPassword());
                serverAccountsService.createAccaunt(serverRegistrationRequest);

                return ResponseEntity.ok(messageSource.getMessage("site.verification.and.registration.success", null, locale));

            } catch (AccountExceptions e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (Exception ex) {
                return ResponseEntity.internalServerError().body(messageSource.getMessage("site.create.error", null, locale));
            }
        } else {
            return ResponseEntity.badRequest().body(messageSource.getMessage("site.verification.code.error", null, locale));
        }
    }

    public ResponseEntity<?> checkRegister(SiteRegistrationRequest regAcc) throws AccountExceptions {
        Locale locale = LocaleContextHolder.getLocale();

        SiteAccounts acc = new SiteAccounts();
        acc.setL2email(regAcc.getL2email());
        acc.setLogin(regAcc.getLogin());
        acc.setRole("USER");
        acc.setPassword(passwordEncoder.encode(regAcc.getPassword()));
        if (siteAccountsRep.findByLogin(regAcc.getLogin()) != null || siteAccountsRep.findByL2email(regAcc.getL2email()) != null) {
            throw new AccountExceptions(messageSource.getMessage("site.account.exists", null, locale));
        }
        // Валідація ентіті
        Set<ConstraintViolation<SiteAccounts>> violations = validator.validate(acc);
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<SiteAccounts> violation : violations) {
                errors.append(messageSource.getMessage(violation.getMessage(), null, locale)).append("; ");
            }
            throw new AccountExceptions(errors.toString());
        }
        return ResponseEntity.ok(messageSource.getMessage("site.checkregister.ok", null, locale));
    }

}
