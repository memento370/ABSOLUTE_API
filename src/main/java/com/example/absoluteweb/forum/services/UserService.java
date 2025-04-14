package com.example.absoluteweb.forum.services;

import com.example.absoluteweb.forum.DTO.ForumRegistrationRequest;
import com.example.absoluteweb.forum.DTO.UserDTO;
import com.example.absoluteweb.forum.entity.User;
import com.example.absoluteweb.forum.exceptions.UserException;
import com.example.absoluteweb.forum.repository.MessageRep;
import com.example.absoluteweb.forum.repository.TopicRep;
import com.example.absoluteweb.forum.repository.UserRep;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private MessageRep messageRepository;

    private UserRep userRepository;

    private TopicRep topicRepository;

    public UserService(MessageRep messageRepository, UserRep userRepository, TopicRep topicRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    public ResponseEntity<User> getUserById(UserDTO user) throws UserException {
       User findUser = new User();
       findUser = userRepository.findById(user.getId()).orElse(null);
       if (findUser == null) {
           throw new UserException("Пользователь не найден");
       }
        return ResponseEntity.ok(findUser);
    }

    public ResponseEntity<User> createUser(ForumRegistrationRequest user) throws UserException {
            User createUser = new User();
            createUser.setEmail(user.getEmail());
            createUser.setPassword(user.getPassword());
            createUser.setNick(user.getNick());
            createUser.setLogin(user.getLogin());
            createUser.setTitle("Vagabond");
            createUser.setStatus("active");
        try{
            userRepository.save(createUser);
            return ResponseEntity.ok(createUser);
        }catch(UserException e){
            throw new UserException(e.getMessage());
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
        return true;
    }
}
