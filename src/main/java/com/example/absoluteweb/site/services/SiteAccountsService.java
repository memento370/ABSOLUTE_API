package com.example.absoluteweb.site.services;

import com.example.absoluteweb.config.JwtUtils;
import com.example.absoluteweb.site.DTO.SiteRegistrationRequest;
import com.example.absoluteweb.site.entity.SiteAccounts;
import com.example.absoluteweb.site.exceptions.AccountExceptions;
import com.example.absoluteweb.site.repository.SiteAccountsRep;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class SiteAccountsService {

    private final SiteAccountsRep siteAccountsRep;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    public SiteAccountsService(SiteAccountsRep siteAccountsRep, PasswordEncoder passwordEncoder) {
        this.siteAccountsRep = siteAccountsRep;
        this.passwordEncoder = passwordEncoder;
    }
    public ResponseEntity<?> createAccaunt(SiteRegistrationRequest regAcc) throws AccountExceptions {
        SiteAccounts acc = new SiteAccounts();
        acc.setL2email(regAcc.getL2email());
        acc.setLogin(regAcc.getLogin());
        acc.setRole("USER");
        if (regAcc.getLogin().length() > 14 || regAcc.getPassword().length() > 14) {
            throw new AccountExceptions("Логин и пароль не могут содержать более 14 символов");
        }
        if (siteAccountsRep.findByLogin(regAcc.getLogin()) != null || siteAccountsRep.findByL2email(regAcc.getL2email()) != null) {
            throw new AccountExceptions("Логин или e-mail уже зарегистрированы на сайте");
        }
        acc.setPassword(passwordEncoder.encode(regAcc.getPassword()));
        try {
            siteAccountsRep.save(acc);
            return ResponseEntity.ok("Ваш акаунт зарегистрирован на сайте");
        } catch (Exception ex) {
            throw new AccountExceptions("Ошибка сохранения на сайте.");
        }
    }

    public Boolean checkRegister(SiteRegistrationRequest regAcc) throws AccountExceptions {
        SiteAccounts acc = new SiteAccounts();
        acc.setL2email(regAcc.getL2email());
        acc.setLogin(regAcc.getLogin());
        acc.setPassword(regAcc.getPassword());
        SiteAccounts accLogin = siteAccountsRep.findByLogin(regAcc.getLogin());
        if(accLogin!=null){
            throw new AccountExceptions("Такой логин уже зарегистрирован");
        }
        SiteAccounts accEmail = siteAccountsRep.findByL2email(regAcc.getL2email());
        if(accEmail!=null){
            throw new AccountExceptions("Такой e-mail уже зарегистрирован");
        }
        if(regAcc.getPassword().isEmpty()){
            throw new AccountExceptions("Пароль не может быть пустым");
        }
        if(regAcc.getLogin().isEmpty()){
            throw new AccountExceptions("Логин не может быть пустым");
        }
        if(regAcc.getL2email().isEmpty()){
            throw new AccountExceptions("E-mail не может быть пустым");
        }
        return true;
    }

    public ResponseEntity login(SiteRegistrationRequest login) throws AccountExceptions {
        SiteAccounts user = siteAccountsRep.findByLogin(login.getLogin());
        if (user == null) {
            throw new AccountExceptions("Логин не найден. Убедитесь в правильности введенных данных.");
        }
        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new AccountExceptions("Пароль не правильный. Убедитесь в правильности введенных данных.");
        }
        String token = jwtUtils.generateToken(user.getLogin(), user.getRole());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole());
        response.put("login", user.getLogin());
        response.put("message", "Авторизация успешна");

        return ResponseEntity.ok(response);
    }


}
