package com.example.absoluteweb.forum.services;

import com.example.absoluteweb.config.JwtUtils;
import com.example.absoluteweb.forum.DTO.ForumRegistrationRequest;
import com.example.absoluteweb.forum.DTO.UserDTO;
import com.example.absoluteweb.forum.entity.User;
import com.example.absoluteweb.forum.exceptions.UserException;
import com.example.absoluteweb.forum.repository.MessageRep;
import com.example.absoluteweb.forum.repository.TopicRep;
import com.example.absoluteweb.forum.repository.UserRep;
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
public class UserService {
    @Autowired
    private JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    private MessageRep messageRepository;

    private UserRep userRepository;

    private TopicRep topicRepository;


    public UserService(MessageRep messageRepository, UserRep userRepository, TopicRep topicRepository,PasswordEncoder passwordEncoder) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<User> getUserById(UserDTO user) throws UserException {
       User findUser = new User();
       findUser = userRepository.findById(user.getId()).orElse(null);
       if (findUser == null) {
           throw new UserException("Пользователь не найден");
       }
        return ResponseEntity.ok(findUser);
    }

    public ResponseEntity createUser(ForumRegistrationRequest user) throws UserException {
            checkRegister(user);
            User createUser = new User();
            createUser.setEmail(user.getEmail());
            createUser.setPassword(user.getPassword());
            createUser.setNick(user.getNick());
            createUser.setLogin(user.getLogin());
            createUser.setTitle("Vagabond");
            createUser.setStatus("active");
        try {
            if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
                Security.addProvider(new BouncyCastleProvider());
            }
            MessageDigest md = MessageDigest.getInstance("WHIRLPOOL", "BC");
            byte[] hashBytes = md.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));
            String encodedPassword = Base64.getEncoder().encodeToString(hashBytes);
            createUser.setPassword(encodedPassword);
        } catch (Exception ex) {
            throw new UserException("Ошибка зашифровки пароля");
        }

        try{
            userRepository.save(createUser);
            return ResponseEntity.ok("Ваш акаунт зарегистрирован на форуме");
        }catch (Exception ex) {
            throw new UserException("Ошибка сохранения на форуме.");
        }
    }
    public ResponseEntity<User> updateUser(UserDTO user) throws UserException {
        User updateUser = userRepository.findById(user.getId()).orElse(null);
        if (updateUser == null) {
            throw new UserException("Пользователь не найден");
        }
        updateUser.setStatus(user.getStatus());
        updateUser.setTitle(user.getTitle());
        updateUser.setLogin(user.getLogin());
        updateUser.setPassword(user.getPassword());
        updateUser.setNick(user.getNick());
        try{
            userRepository.save(updateUser);
            return ResponseEntity.ok(updateUser);
        }catch (UserException e){
            throw new UserException(e.getMessage());
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

    public ResponseEntity login(ForumRegistrationRequest login) throws UserException {
        User user = userRepository.findByLogin(login.getLogin());
        if (user == null) {
            throw new UserException("Логин не найден. Убедитесь в правильности введенных данных.");
        }
        if (!verifyPassword(login.getPassword(), user.getPassword())) {
            throw new UserException("Пароль не правильный. Убедитесь в правильности введенных данных.");
        }
        String token = jwtUtils.generateTokenForum(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("status", user.getStatus());
        response.put("nick", user.getNick());
        response.put("title", user.getTitle());
        response.put("role", user.getRole());
        response.put("message", "Авторизация успешна");

        return ResponseEntity.ok(response);
    }
    private boolean verifyPassword(String rawPassword, String storedHash) throws UserException {
        try {
            MessageDigest md = MessageDigest.getInstance("WHIRLPOOL", "BC");
            byte[] hashBytes = md.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            String encodedInputPassword = Base64.getEncoder().encodeToString(hashBytes);
            return encodedInputPassword.equals(storedHash);
        } catch (Exception ex) {
            throw new UserException("Ошибка зашифровки пароля");
        }
    }
    public ResponseEntity restorePassword(String email,String password) throws UserException {
        System.out.println("email : "+email);
        System.out.println("pass : "+password);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("Е-мейл не найден.Убедитесь в правильности данных.");
        }
        try {
            if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
                Security.addProvider(new BouncyCastleProvider());
            }
            MessageDigest md = MessageDigest.getInstance("WHIRLPOOL", "BC");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            String encodedPassword = Base64.getEncoder().encodeToString(hashBytes);
            user.setPassword(encodedPassword);
        } catch (Exception ex) {
            throw new UserException("Ошибка зашифровки пароля");
        }
        userRepository.save(user);
        return ResponseEntity.ok("Пароль успешно изменён");
    }

}
