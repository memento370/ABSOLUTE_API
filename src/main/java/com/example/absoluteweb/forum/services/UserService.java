package com.example.absoluteweb.forum.services;

import com.example.absoluteweb.config.JwtUtils;
import com.example.absoluteweb.forum.DTO.ForumRegistrationRequest;
import com.example.absoluteweb.forum.DTO.UserDTO;
import com.example.absoluteweb.forum.entity.User;
import com.example.absoluteweb.forum.exceptions.UserException;
import com.example.absoluteweb.forum.repository.CommentTopicRep;
import com.example.absoluteweb.forum.repository.TopicRep;
import com.example.absoluteweb.forum.repository.UserRep;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private final JwtUtils jwtUtils;
    private final EmailServiceForum emailServiceForum;
    private final PasswordEncoder passwordEncoder;
    private final CommentTopicRep commentTopicRepository;
    private final UserRep userRepository;
    private final TopicRep topicRepository;
    private final Storage storage;
    private final String BUCKET_NAME = "l2-absolute-forum-one";



    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Map<String, String> verificationCodesRestore = new HashMap<>();
    private final Map<String, String> verificationCodesLogin = new HashMap<>();
    private final Map<String, String> verificationCodesOldEmail = new HashMap<>();
    private final Map<String, String> verificationCodesNewEmail = new HashMap<>();
    private final Map<String, String> verificationCodesPassword = new HashMap<>();


    @Autowired
    public UserService(
            CommentTopicRep commentTopicRepository,
            UserRep userRepository,
            TopicRep topicRepository,
            PasswordEncoder passwordEncoder,
            Storage storage,
            EmailServiceForum emailServiceForum,
            JwtUtils jwtUtils
    ) {
        this.commentTopicRepository = commentTopicRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.passwordEncoder = passwordEncoder;
        this.storage = storage;
        this.emailServiceForum = emailServiceForum;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<UserDTO> getUserById(Long id) throws UserException {
        User findUser = userRepository.findById(id).orElse(null);
       if (findUser == null) {
           throw new UserException("Пользователь не найден");
       }
       UserDTO user = new UserDTO();
       user.setId(findUser.getId());
       user.setEmail(findUser.getEmail());
       user.setLogin(findUser.getLogin());
       user.setNick(findUser.getNick());
       user.setStatus(findUser.getStatus());
       user.setTitle(findUser.getTitle());
       user.setAvatarUrl(findUser.getAvatarUrl());
       user.setRole(findUser.getRole());

        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> createUser(ForumRegistrationRequest user) throws UserException {
        checkRegister(user);
        User createUser = new User();
        createUser.setEmail(user.getEmail());
        createUser.setNick(user.getNick());
        createUser.setLogin(user.getLogin());
        createUser.setTitle("Vagabond");
        createUser.setStatus("active");
        createUser.setRole("USER");
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            userRepository.save(createUser);
            return ResponseEntity.ok("Ваш акаунт зарегистрирован на форуме");
        } catch (Exception ex) {
            throw new UserException("Ошибка сохранения на форуме.");
        }
    }

    public Boolean checkRegister(ForumRegistrationRequest regAcc) throws UserException {
        User acc = new User();
        acc.setEmail(regAcc.getEmail());
        acc.setLogin(regAcc.getLogin());
        acc.setPassword(regAcc.getPassword());
        acc.setNick(regAcc.getNick());
        acc.setTitle(regAcc.getTitle());
        acc.setStatus(regAcc.getStatus());
        User accLogin = userRepository.findByLogin(regAcc.getLogin());
        if(accLogin!=null){
            throw new UserException("Такой логин уже зарегистрирован");
        }
        User accEmail = userRepository.findByEmail(regAcc.getEmail());
        if(accEmail!=null){
            throw new UserException("Такой e-mail уже зарегистрирован");
        }
        User accNick = userRepository.findByNick(regAcc.getNick());
        if(accNick!=null){
            throw new UserException("Такой никнейм уже зарегистрирован");
        }
        if(regAcc.getPassword().isEmpty()){
            throw new UserException("Пароль не может быть пустым");
        }
        if(regAcc.getLogin().isEmpty()){
            throw new UserException("Логин не может быть пустым");
        }
        if(regAcc.getEmail().isEmpty()){
            throw new UserException("E-mail не может быть пустым");
        }
        if(regAcc.getNick().isEmpty()){
            throw new UserException("Никнейм не может быть пустым");
        }
        if(regAcc.getLogin().length()>40 || regAcc.getPassword().length()>40 ||
            regAcc.getNick().length()>40 || regAcc.getEmail().length()>40) {
            throw new UserException("Логин ,пароль,е-мейл,никнейм не могут содержать более 40 символов");
        }
        return true;
    }

    public ResponseEntity<?> login(ForumRegistrationRequest login) throws UserException {
        User user = userRepository.findByLogin(login.getLogin());
        if (user == null) {
            throw new UserException("Логин не найден. Убедитесь в правильности введенных данных.");
        }
        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new UserException("Пароль не правильний. Убедитесь в правильности введенных данных.");
        }
        String token = jwtUtils.generateTokenForum(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("status", user.getStatus());
        response.put("nick", user.getNick());
        response.put("title", user.getTitle());
        response.put("role", user.getRole());
        response.put("user_id", user.getId().toString());
        response.put("avatar_url", user.getAvatarUrl());
        response.put("message", "Авторизация успешна");

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> restorePassword(String email, String password) throws UserException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("Е-мейл не найден.Убедитесь в правильности данных.");
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return ResponseEntity.ok("Пароль успешно изменён");
    }

    public String uploadUserAvatar(Long userId, String base64Image) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getAvatarUrl() != null && !user.getAvatarUrl().isBlank()) {
            deleteBlobByUrl(user.getAvatarUrl());
        }
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        String fileName = "avatars/user_" + userId + "_" + UUID.randomUUID() + ".png";
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("image/png")
                .build();
        storage.create(blobInfo, imageBytes);
        String avatarUrl = String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, fileName);
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
        return avatarUrl;
    }

    public void deleteUserAvatar(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getAvatarUrl() != null && !user.getAvatarUrl().isBlank()) {
            deleteBlobByUrl(user.getAvatarUrl());
            user.setAvatarUrl(null);
            userRepository.save(user);
        }
    }

    private void deleteBlobByUrl(String url) {
        String prefix = "https://storage.googleapis.com/" + BUCKET_NAME + "/";
        if (url.startsWith(prefix)) {
            String blobName = url.substring(prefix.length());
            storage.delete(BlobId.of(BUCKET_NAME, blobName));
        }
    }

    public UserDTO updateUserProfile(Long userId, UserDTO dto) throws UserException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
        if(dto.getNick().length()>40){
            throw new UserException("Нікнейм не може містити більше 40 символів");
        }
        if(dto.getTitle().length()>40){
            throw new UserException("Титул не може містити більше 40 символів");
        }
        user.setNick(dto.getNick());
        user.setTitle(dto.getTitle());
        user.setStatus(dto.getStatus());

        userRepository.save(user);
        UserDTO result = new UserDTO();

        result.setId(user.getId());
        result.setNick(user.getNick());
        result.setTitle(user.getTitle());
        result.setStatus(user.getStatus());
        result.setLogin(user.getLogin());
        result.setAvatarUrl(user.getAvatarUrl());
        result.setEmail(user.getEmail());
        result.setRole(user.getRole());
        return result;
    }

    public String sendVerificationCode(String email) throws MailSendException {
        String code = emailServiceForum.sendVerificationEmail(email);
        verificationCodes.put(email, code);
        return "Код підтвердження відправлено на " + email;
    }

    public boolean verifyCode(String email, String code) {
        if (verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code)) {
            verificationCodes.remove(email);
            return true;
        }
        return false;
    }

    public String sendVerificationCodeRestore(String email) throws MailSendException {
        String code = emailServiceForum.sendVerificationEmailRestore(email);
        verificationCodesRestore.put(email, code);
        return "Код підтвердження відправлено на " + email;
    }

    public boolean verifyCodeRestore(String email, String code) {
        if (verificationCodesRestore.containsKey(email) && verificationCodesRestore.get(email).equals(code)) {
            verificationCodesRestore.remove(email);
            return true;
        }
        return false;
    }
    public String sendVerificationCodeLogin(Long userId) throws MailSendException,UserException{
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
        String email = user.getEmail();
        if (email == null || email.isBlank()) {
            throw new UserException("E-mail відсутній у профілі");
        }
        String code = emailServiceForum.sendVerificationChangeLogin(email);
        verificationCodesLogin.put(email, code);
        return "Код підтвердження відправлено на " + email;
    }

    public String changeLogin(Long userId, String newLogin, String code) throws UserException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
        String email = user.getEmail();
        if (!verificationCodesLogin.containsKey(email) || !verificationCodesLogin.get(email).equals(code)) {
            throw new RuntimeException("Невірний код підтвердження.");
        }
        if (userRepository.findByLogin(newLogin) != null) {
            throw new RuntimeException("Такий логін вже існує.");
        }
        user.setLogin(newLogin);
        userRepository.save(user);
        verificationCodesLogin.remove(email);
        return "Логін успішно змінено.";
    }

    public String sendVerificationCodeOldEmail(Long userId) throws UserException,MailSendException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
        String oldEmail = user.getEmail();
        if (oldEmail == null || oldEmail.isBlank()) {
            throw new UserException("У профілі не вказано e-mail");
        }
        String code = emailServiceForum.sendVerificationChangeOldEmail(oldEmail);
        verificationCodesOldEmail.put(oldEmail, code);
        return "Код підтвердження надіслано на старий e-mail";
    }

    public String sendVerificationCodeNewEmail(Long userId, String newEmail) throws UserException,MailSendException {
        if (newEmail == null || newEmail.isBlank()) {
            throw new UserException("Введіть новий e-mail");
        }
        if (userRepository.findByEmail(newEmail) != null) {
            throw new UserException("Такий e-mail вже використовується");
        }
        String code = emailServiceForum.sendVerificationEmail(newEmail);
        verificationCodesNewEmail.put(newEmail, code);
        return "Код підтвердження надіслано на новий e-mail";
    }

    public String changeEmail(Long userId, String oldEmailCode, String newEmail, String newEmailCode) throws UserException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
        String oldEmail = user.getEmail();
        if (!verificationCodesOldEmail.containsKey(oldEmail) || !verificationCodesOldEmail.get(oldEmail).equals(oldEmailCode)) {
            throw new UserException("Невірний код зі старого e-mail.");
        }
        if (!verificationCodesNewEmail.containsKey(newEmail) || !verificationCodesNewEmail.get(newEmail).equals(newEmailCode)) {
            throw new UserException("Невірний код з нового e-mail.");
        }
        if (userRepository.findByEmail(newEmail) != null) {
            throw new UserException("Такий e-mail вже використовується.");
        }
        user.setEmail(newEmail);
        userRepository.save(user);
        verificationCodesOldEmail.remove(oldEmail);
        verificationCodesNewEmail.remove(newEmail);
        return "E-mail успішно змінено.";
    }
    public String sendVerificationCodePassword(Long userId) throws UserException,MailSendException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
        String email = user.getEmail();
        if (email == null || email.isBlank()) {
            throw new UserException("У профілі не вказано e-mail");
        }
        String code = emailServiceForum.sendVerificationPassword(email);
        verificationCodesPassword.put(email, code);
        return "Код підтвердження надіслано на e-mail";
    }

    public String changePassword(Long userId, String oldPassword, String newPassword, String code) throws UserException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
        String email = user.getEmail();
        if (!verificationCodesPassword.containsKey(email) || !verificationCodesPassword.get(email).equals(code)) {
            throw new UserException("Невірний код підтвердження.");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UserException("Старий пароль неправильний.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        verificationCodesPassword.remove(email);
        return "Пароль успішно змінено.";
    }

}


