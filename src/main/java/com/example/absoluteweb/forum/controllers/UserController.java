package com.example.absoluteweb.forum.controllers;

import com.example.absoluteweb.config.JwtUtils;
import com.example.absoluteweb.forum.DTO.AvatarUploadRequest;
import com.example.absoluteweb.forum.DTO.ForumRegistrationRequest;
import com.example.absoluteweb.forum.DTO.UserDTO;
import com.example.absoluteweb.forum.entity.User;
import com.example.absoluteweb.forum.exceptions.UserException;
import com.example.absoluteweb.forum.principals.UserPrincipal;
import com.example.absoluteweb.forum.repository.UserRep;
import com.example.absoluteweb.forum.services.EmailServiceForum;
import com.example.absoluteweb.forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/forum/user")
public class UserController {

    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Map<String, String> verificationCodesRestore = new HashMap<>();
    private final Map<String, String> verificationCodesLogin = new HashMap<>();
    private final Map<String, String> verificationCodesOldEmail = new HashMap<>();
    private final Map<String, String> verificationCodesNewEmail = new HashMap<>();
    private final Map<String, String> verificationCodesPassword = new HashMap<>();

    private final UserRep userRepository;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, JwtUtils jwtUtils,UserRep userRepository) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        try {
            return userService.getUserById(id);
        } catch (UserException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody ForumRegistrationRequest user) {
        try{
            return userService.createUser(user);
        }catch(UserException e){
            return ResponseEntity.badRequest().body(e.getMessage());
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
        try {
            String email = request.get("email");
            String result = userService.sendVerificationCode(email);
            return ResponseEntity.ok(result);
        } catch (MailSendException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Введён некоректный e-mail");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        boolean isValid = userService.verifyCode(email, code);

        if (isValid) {
            return ResponseEntity.ok("Код підтверджено");
        } else {
            return ResponseEntity.badRequest().body("Введено невірний код.");
        }
    }

    @PostMapping("/check-register")
    public ResponseEntity<?> checkRegister(@RequestBody ForumRegistrationRequest regAcc) {
        try {
            userService.checkRegister(regAcc);
            return ResponseEntity.ok("login and e-mail valid");
        }
        catch (UserException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ForumRegistrationRequest login) {
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
    @PostMapping("/check-user-token")
    public Boolean checkToken(@RequestBody UserDTO user) {
        return jwtUtils.validateTokenForum(user);
    }

    @PostMapping("/restore-password")
    public ResponseEntity<?> restorePassword(@RequestBody Map<String, String> request) {
        try{
            return userService.restorePassword(request.get("email"),request.get("pass"));
        } catch (UserException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PostMapping("/send-verification-restore")
    public ResponseEntity<String> sendVerificationCodeRestore(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String result = userService.sendVerificationCodeRestore(email);
            return ResponseEntity.ok(result);
        } catch (MailSendException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Введён некоректный e-mail");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify-code-restore")
    public ResponseEntity<String> verifyCodeRestore(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        boolean isValid = userService.verifyCodeRestore(email, code);
        if (isValid) {
            return ResponseEntity.ok("Код підтверджено");
        } else {
            return ResponseEntity.badRequest().body("Введено невірний код.");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestBody AvatarUploadRequest request,
                                               @AuthenticationPrincipal UserPrincipal principal) {

        String avatarUrl = userService.uploadUserAvatar(principal.id(), request.getBase64Image());
        return ResponseEntity.ok(avatarUrl);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete-avatar")
    public ResponseEntity<Void> deleteAvatar(@AuthenticationPrincipal UserPrincipal principal) {

        userService.deleteUserAvatar(principal.id());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update-profile")
    public ResponseEntity<UserDTO> updateProfile(
            @RequestBody UserDTO request,
            @AuthenticationPrincipal UserPrincipal principal) {

        UserDTO updated = userService.updateUserProfile(principal.id(), request);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/send-verification-login")
    public ResponseEntity<String> sendVerificationCodeLogin(@AuthenticationPrincipal UserPrincipal principal) {
        try {
            String result = userService.sendVerificationCodeLogin(principal.id());
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/change-login")
    public ResponseEntity<String> changeLogin(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserPrincipal principal) {

        String newLogin = request.get("login");
        String code = request.get("code");
        try {
            String result = userService.changeLogin(principal.id(), newLogin, code);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/send-verification-old-email")
    public ResponseEntity<String> sendVerificationCodeOldEmail(@AuthenticationPrincipal UserPrincipal principal) {

        try {
            String result = userService.sendVerificationCodeOldEmail(principal.id());
            return ResponseEntity.ok(result);
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/send-verification-new-email")
    public ResponseEntity<String> sendVerificationCodeNewEmail(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserPrincipal principal) {

        String newEmail = request.get("newEmail");
        try {
            String result = userService.sendVerificationCodeNewEmail(principal.id(), newEmail);
            return ResponseEntity.ok(result);
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/change-email")
    public ResponseEntity<String> changeEmail(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserPrincipal principal) {

        String oldEmailCode = request.get("oldEmailCode");
        String newEmail = request.get("newEmail");
        String newEmailCode = request.get("newEmailCode");
        try {
            String result = userService.changeEmail(principal.id(), oldEmailCode, newEmail, newEmailCode);
            return ResponseEntity.ok(result);
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/send-verification-password")
    public ResponseEntity<String> sendVerificationCodePassword(@AuthenticationPrincipal UserPrincipal principal) {
        try {
            String result = userService.sendVerificationCodePassword(principal.id());
            return ResponseEntity.ok(result);
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserPrincipal principal) {

        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");
        String code = request.get("code");
        try {
            String result = userService.changePassword(principal.id(), oldPassword, newPassword, code);
            return ResponseEntity.ok(result);
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
