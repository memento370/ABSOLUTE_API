package com.example.absoluteweb.forum.controllers;

import com.example.absoluteweb.config.JwtUtils;
import com.example.absoluteweb.forum.DTO.ForumRegistrationRequest;
import com.example.absoluteweb.forum.DTO.UserDTO;
import com.example.absoluteweb.forum.entity.User;
import com.example.absoluteweb.forum.exceptions.UserException;
import com.example.absoluteweb.forum.repository.UserRep;
import com.example.absoluteweb.forum.services.EmailServiceForum;
import com.example.absoluteweb.forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/forum/user")
public class UserController {

    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Map<String, String> verificationCodesRestore = new HashMap<>();

    @Autowired
    private UserRep userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailServiceForum emailServiceForum;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/get-user/{id}")
    public ResponseEntity<User> getUserById(@RequestBody UserDTO user) {
    try{
            return userService.getUserById(user);
        }catch(UserException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity createUser(@RequestBody ForumRegistrationRequest user) {
        try{
            return userService.createUser(user);
        }catch(UserException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody UserDTO user) {
        try{
            return userService.updateUser(user);
        }catch (UserException e){
            return ResponseEntity.badRequest().build();
        }

    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        return userRepository.findById(id).map(user -> {
//            userRepository.delete(user);
//            return ResponseEntity.ok().<Void>build();
//        }).orElse(ResponseEntity.notFound().build());
//    }


    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        try{
            String code = emailServiceForum.sendVerificationEmail(email);
            verificationCodes.put(email, code);
            return ResponseEntity.ok("Код підтвердження відправлено на " + email);
        }catch (MailSendException e){
            System.out.println(e.getMessage());
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

    @PostMapping("/check-register")
    private ResponseEntity checkRegister(@RequestBody ForumRegistrationRequest regAcc) {
        try {
            userService.checkRegister(regAcc);
            return ResponseEntity.ok("login and e-mail valid");
        }
        catch (UserException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody ForumRegistrationRequest login) {
        try{
            return userService.login(login);
        } catch (UserException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/check-token")
    public Boolean checkToken(@RequestBody String token) {
        return jwtUtils.validateTokenForum(token);
    }

    @PostMapping("/restore-password")
    public ResponseEntity restorePassword(@RequestBody Map<String, String> request) {
        try{
            return userService.restorePassword(request.get("email"),request.get("pass"));
        } catch (UserException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PostMapping("/send-verification-restore")
    public ResponseEntity<String> sendVerificationCodeRestore(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        try{
            String code = emailServiceForum.sendVerificationEmailRestore(email);
            verificationCodesRestore.put(email, code);
            return ResponseEntity.ok("Код підтвердження відправлено на " + email);
        }catch (MailSendException e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Введён некоректный e-mail");
        }
    }

    @PostMapping("/verify-code-restore")
    public ResponseEntity<String> verifyCodeRestore(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        if (verificationCodesRestore.containsKey(email) && verificationCodesRestore.get(email).equals(code)) {
            verificationCodesRestore.remove(email);
            return ResponseEntity.ok("Код подтверждён");
        } else {
            return ResponseEntity.badRequest().body("Введен неверный код.");
        }
    }
}
