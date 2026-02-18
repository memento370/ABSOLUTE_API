package com.example.absoluteweb.site.services;

import com.example.absoluteweb.config.JwtUtils;
import com.example.absoluteweb.server.DTO.ServerRegistrationRequest;
import com.example.absoluteweb.server.services.ServerAccountsService;
import com.example.absoluteweb.site.DTO.*;
import com.example.absoluteweb.site.entity.SiteAccounts;
import com.example.absoluteweb.site.exceptions.AccountExceptions;
import com.example.absoluteweb.site.principals.AccountPrincipal;
import com.example.absoluteweb.site.repository.SiteAccountsRep;
import org.springframework.transaction.annotation.Transactional;
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
import org.springframework.transaction.annotation.Transactional;


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
        response.put("l2email", user.getL2email());
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

    public ResponseEntity<?> requestEmailChangeCode(AccountPrincipal principal) throws AccountExceptions {
        Locale locale = LocaleContextHolder.getLocale();
        String login = principal.login();

        SiteAccounts acc = siteAccountsRep.findByLogin(login);
        if (acc == null) {
            throw new AccountExceptions(messageSource.getMessage("site.email.change.account.notfound", null, locale));
        }

        String oldEmail = acc.getL2email();
        if (oldEmail == null || oldEmail.isBlank()) {
            throw new AccountExceptions(messageSource.getMessage("site.email.change.oldEmail.missing", null, locale));
        }

        String code = emailService.sendEmailChangeVerification(oldEmail);
        verificationCodeService.saveEmailChangeCode(oldEmail, code);

        return ResponseEntity.ok(messageSource.getMessage("site.email.change.request.ok", null, locale));
    }

    @Transactional(transactionManager = "siteTransactionManager")
    public ResponseEntity<?> confirmEmailChange(AccountPrincipal principal, EmailChangeConfirmDTO dto) throws AccountExceptions {
        Locale locale = LocaleContextHolder.getLocale();
        String login = principal.login();

        SiteAccounts acc = siteAccountsRep.findByLogin(login);
        if (acc == null) {
            throw new AccountExceptions(messageSource.getMessage("site.email.change.account.notfound", null, locale));
        }

        String oldEmail = acc.getL2email();
        if (oldEmail == null || oldEmail.isBlank()) {
            throw new AccountExceptions(messageSource.getMessage("site.email.change.oldEmail.missing", null, locale));
        }

        String newEmail = dto.getNewEmail();
        String code = dto.getCode();

        if (oldEmail.equalsIgnoreCase(newEmail)) {
            throw new AccountExceptions(messageSource.getMessage("site.email.change.newEmail.sameAsOld", null, locale));
        }

        String cachedCode = verificationCodeService.getEmailChangeCode(oldEmail);
        if (cachedCode == null || cachedCode.isBlank()) {
            throw new AccountExceptions(messageSource.getMessage("site.email.change.code.expired", null, locale));
        }

        if (!cachedCode.equals(code)) {
            throw new AccountExceptions(messageSource.getMessage("site.email.change.code.invalid", null, locale));
        }

        SiteAccounts byEmail = siteAccountsRep.findByL2email(newEmail);
        if (byEmail != null) {
            throw new AccountExceptions(messageSource.getMessage("site.email.change.newEmail.inUse", null, locale));
        }

        acc.setL2email(newEmail);
        siteAccountsRep.save(acc);
        verificationCodeService.deleteEmailChangeCode(oldEmail);

        return ResponseEntity.ok(messageSource.getMessage("site.email.change.confirm.ok", null, locale));
    }

    public ResponseEntity<?> remindLogin(AccountPrincipal principal) throws AccountExceptions {
        Locale locale = LocaleContextHolder.getLocale();
        String login = principal.login();

        SiteAccounts acc = siteAccountsRep.findByLogin(login);
        if (acc == null) {
            throw new AccountExceptions(messageSource.getMessage("site.login.remind.account.notfound", null, locale));
        }

        String email = acc.getL2email();
        if (email == null || email.isBlank()) {
            throw new AccountExceptions(messageSource.getMessage("site.login.remind.email.missing", null, locale));
        }

        emailService.sendLoginReminder(email, login);

        return ResponseEntity.ok(messageSource.getMessage("site.login.remind.ok", null, locale));
    }

    public ResponseEntity<?> requestPasswordChangeCode(AccountPrincipal principal) throws AccountExceptions {
        Locale locale = LocaleContextHolder.getLocale();
        String login = principal.login();

        SiteAccounts acc = siteAccountsRep.findByLogin(login);
        if (acc == null) {
            throw new AccountExceptions(messageSource.getMessage("site.password.change.account.notfound", null, locale));
        }

        String email = acc.getL2email();
        if (email == null || email.isBlank()) {
            throw new AccountExceptions(messageSource.getMessage("site.password.change.email.missing", null, locale));
        }

        String code = emailService.sendPasswordChangeVerification(email);
        verificationCodeService.savePasswordChangeCode(email, code);

        return ResponseEntity.ok(messageSource.getMessage("site.password.change.request.ok", null, locale));
    }

    @Transactional(transactionManager = "siteTransactionManager")
    public ResponseEntity<?> confirmPasswordChange(AccountPrincipal principal, PasswordChangeConfirmDTO dto) throws AccountExceptions {
        Locale locale = LocaleContextHolder.getLocale();
        String login = principal.login();

        SiteAccounts acc = siteAccountsRep.findByLogin(login);
        if (acc == null) {
            throw new AccountExceptions(messageSource.getMessage("site.password.change.account.notfound", null, locale));
        }

        String email = acc.getL2email();
        if (email == null || email.isBlank()) {
            throw new AccountExceptions(messageSource.getMessage("site.password.change.email.missing", null, locale));
        }

        String newPassword = dto.getNewPassword().trim();
        String code = dto.getCode().trim();

        String cachedCode = verificationCodeService.getPasswordChangeCode(email);
        if (cachedCode == null || cachedCode.isBlank()) {
            throw new AccountExceptions(messageSource.getMessage("site.password.change.code.expired", null, locale));
        }

        if (!cachedCode.equals(code)) {
            throw new AccountExceptions(messageSource.getMessage("site.password.change.code.invalid", null, locale));
        }

        acc.setPassword(passwordEncoder.encode(newPassword));
        siteAccountsRep.save(acc);
        serverAccountsService.changePasswordInternal(acc.getLogin(), newPassword);

        verificationCodeService.deletePasswordChangeCode(email);

        return ResponseEntity.ok(messageSource.getMessage("site.password.change.confirm.ok", null, locale));
    }

    public ResponseEntity<?> requestPasswordReset(PasswordResetRequestDTO dto) throws AccountExceptions {
        Locale locale = LocaleContextHolder.getLocale();
        String email = dto.getEmail().trim();
        SiteAccounts acc = siteAccountsRep.findByL2email(email);

        if (acc != null) {
            String login = acc.getLogin();
            String code = emailService.sendPasswordResetEmail(email, login);
            verificationCodeService.savePasswordResetCode(email, code);
        }

        return ResponseEntity.ok(messageSource.getMessage("site.password.reset.request.ok", null, locale));
    }

    @Transactional(transactionManager = "siteTransactionManager")
    public ResponseEntity<?> confirmPasswordReset(PasswordResetConfirmDTO dto) throws AccountExceptions {
        Locale locale = LocaleContextHolder.getLocale();

        String email = dto.getEmail().trim();
        String code = dto.getCode().trim();
        String newPassword = dto.getNewPassword();

        SiteAccounts acc = siteAccountsRep.findByL2email(email);
        if (acc == null) {
            throw new AccountExceptions(messageSource.getMessage("site.password.reset.code.invalid", null, locale));
        }

        String cachedCode = verificationCodeService.getPasswordResetCode(email);
        if (cachedCode == null || cachedCode.isBlank()) {
            throw new AccountExceptions(messageSource.getMessage("site.password.reset.code.expired", null, locale));
        }
        if (!cachedCode.equals(code)) {
            throw new AccountExceptions(messageSource.getMessage("site.password.reset.code.invalid", null, locale));
        }

        acc.setPassword(passwordEncoder.encode(newPassword));
        siteAccountsRep.save(acc);

        serverAccountsService.changePasswordInternal(acc.getLogin(), newPassword);
        verificationCodeService.deletePasswordResetCode(email);

        return ResponseEntity.ok(messageSource.getMessage("site.password.reset.confirm.ok", null, locale));
    }



}
