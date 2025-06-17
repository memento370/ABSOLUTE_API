package com.example.absoluteweb.forum.services;

import com.example.absoluteweb.forum.DTO.CommentTopicDTO;
import com.example.absoluteweb.forum.DTO.UserDTO;
import com.example.absoluteweb.forum.entity.CommentTopic;
import com.example.absoluteweb.forum.entity.Topic;
import com.example.absoluteweb.forum.entity.User;
import com.example.absoluteweb.forum.exceptions.CommentTopicException;
import com.example.absoluteweb.forum.principals.UserPrincipal;
import com.example.absoluteweb.forum.repository.CommentTopicRep;
import com.example.absoluteweb.forum.repository.TopicRep;
import com.example.absoluteweb.forum.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentTopicService {
    private final CommentTopicRep commentTopicRepository;
    private final UserRep userRepository;
    private final TopicRep topicRepository;

    @Autowired
    public CommentTopicService(CommentTopicRep commentTopicRepository,
                               UserRep userRepository,
                               TopicRep topicRepository) {
        this.commentTopicRepository = commentTopicRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    public ResponseEntity<?> createComment(CommentTopicDTO dto, UserPrincipal principal) throws CommentTopicException {
        User user = userRepository.findById(principal.getId()).orElse(null);
        Topic topic = topicRepository.findById(dto.getTopicId()).orElse(null);
        if (user == null || topic == null) {
            throw new CommentTopicException("Автор коментаря не знайдений");
        }
        if(dto.getText().length()>500){
            throw new CommentTopicException("Коментар максимум 500 символів");
        }
        if(dto.getText().isBlank()){
            throw new CommentTopicException("Коментар не може бути пустим або складатися тільки із пробілів");
        }
        CommentTopic comment = new CommentTopic();
        comment.setCreatedBy(user);
        comment.setTopic(topic);
        comment.setText(dto.getText());
        comment.setCreationDate(LocalDateTime.now());

        CommentTopic saved = commentTopicRepository.save(comment);

        CommentTopicDTO savedDto = new CommentTopicDTO();
        savedDto.setId(saved.getId());
        savedDto.setText(saved.getText());
        savedDto.setCreationDate(saved.getCreationDate());
        savedDto.setTopicId(saved.getTopic().getId());

        User createdBy = saved.getCreatedBy();
        UserDTO userDto = new UserDTO();
        userDto.setId(createdBy.getId());
        userDto.setStatus(createdBy.getStatus());
        userDto.setTitle(createdBy.getTitle());
        userDto.setLogin(createdBy.getLogin());
        userDto.setPassword("");
        userDto.setNick(createdBy.getNick());
        userDto.setEmail(createdBy.getEmail());
        userDto.setAvatarUrl(createdBy.getAvatarUrl());
        userDto.setRole(createdBy.getRole());
        userDto.setToken("");

        savedDto.setCreatedBy(userDto);

        return ResponseEntity.ok(savedDto);
    }

    public ResponseEntity<?> updateComment(Long id, CommentTopicDTO dto, UserPrincipal principal) throws CommentTopicException {
        CommentTopic comment = commentTopicRepository.findById(id).orElse(null);
        if (comment == null) throw new CommentTopicException("Коментан не знайдено");

        if (!comment.getCreatedBy().getId().equals(principal.getId())) {
            throw new CommentTopicException("Ви не можете редагувати чужий комментар");
        }
        if(dto.getText().length()>500){
            throw new CommentTopicException("Коментар максимум 500 символів");
        }
        if(dto.getText().isBlank()){
            throw new CommentTopicException("Коментар не може бути пустим або складатися тільки із пробілів");
        }

        comment.setText(dto.getText());
        CommentTopic saved = commentTopicRepository.save(comment);

        CommentTopicDTO savedDto = new CommentTopicDTO();
        savedDto.setId(saved.getId());
        savedDto.setText(saved.getText());
        savedDto.setCreationDate(saved.getCreationDate());
        savedDto.setTopicId(saved.getTopic().getId());

        User createdBy = saved.getCreatedBy();
        UserDTO userDto = new UserDTO();
        userDto.setId(createdBy.getId());
        userDto.setStatus(createdBy.getStatus());
        userDto.setTitle(createdBy.getTitle());
        userDto.setLogin(createdBy.getLogin());
        userDto.setPassword("");
        userDto.setNick(createdBy.getNick());
        userDto.setEmail(createdBy.getEmail());
        userDto.setAvatarUrl(createdBy.getAvatarUrl());
        userDto.setRole(createdBy.getRole());
        userDto.setToken("");

        savedDto.setCreatedBy(userDto);

        return ResponseEntity.ok(savedDto);
    }


    public ResponseEntity<?> deleteComment(Long id, UserPrincipal principal) throws CommentTopicException {
        CommentTopic comment = commentTopicRepository.findById(id).orElse(null);
        if (comment == null) throw new CommentTopicException("Comment not found");

        if (!comment.getCreatedBy().getId().equals(principal.getId())) {
            throw new CommentTopicException("No permission to delete this comment");
        }

        commentTopicRepository.delete(comment);
        return ResponseEntity.ok("Comment deleted successfully");
    }
    public ResponseEntity<List<CommentTopicDTO>> getCommentsByTopic(Long topicId) {
        List<CommentTopic> comments = commentTopicRepository.findByTopicId(topicId);
        List<CommentTopicDTO> dtos = new ArrayList<>();

        for (CommentTopic comment : comments) {
            CommentTopicDTO dto = new CommentTopicDTO();
            dto.setId(comment.getId());
            dto.setText(comment.getText());
            dto.setCreationDate(comment.getCreationDate());
            dto.setTopicId(comment.getTopic().getId());

            User createdBy = comment.getCreatedBy();
            UserDTO userDto = new UserDTO();
            userDto.setId(createdBy.getId());
            userDto.setStatus(createdBy.getStatus());
            userDto.setTitle(createdBy.getTitle());
            userDto.setLogin(createdBy.getLogin());
            userDto.setPassword("");
            userDto.setNick(createdBy.getNick());
            userDto.setEmail(createdBy.getEmail());
            userDto.setAvatarUrl(createdBy.getAvatarUrl());
            userDto.setRole(createdBy.getRole());
            userDto.setToken("");

            dto.setCreatedBy(userDto);

            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }
}
