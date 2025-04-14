package com.example.absoluteweb.forum.services;

import com.example.absoluteweb.forum.DTO.TopicMessageDTO;
import com.example.absoluteweb.forum.entity.Message;
import com.example.absoluteweb.forum.entity.Topic;
import com.example.absoluteweb.forum.entity.User;
import com.example.absoluteweb.forum.exceptions.TopicMessageException;
import com.example.absoluteweb.forum.repository.MessageRep;
import com.example.absoluteweb.forum.repository.TopicRep;
import com.example.absoluteweb.forum.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRep messageRepository;

    @Autowired
    private UserRep userRepository;

    @Autowired
    private TopicRep topicRepository;

    public ResponseEntity<List<Message>> getTopicMessages(Long topic) {
        List<Message> messages = messageRepository.findByTopicId(topic);
        messages.sort(Comparator.comparing(Message::getCreationDate));
        return ResponseEntity.ok(messages);
    }
    public ResponseEntity addTopicMessage(TopicMessageDTO message) throws TopicMessageException {
        User user = userRepository.findById(message.getId()).orElse(null);
        Topic topic = topicRepository.findById(message.getId()).orElse(null);
        if (user == null || topic == null) {
            return ResponseEntity.badRequest().build();
        }
        Message msg = new Message();
        msg.setCreatedBy(user);
        msg.setTopic(topic);
        msg.setCreationDate(LocalDateTime.now());
        try{
            Message savedMessage = messageRepository.save(msg);
            return ResponseEntity.ok(savedMessage);
        }catch (Exception e) {
            throw new TopicMessageException("Ошибка создания темы");
        }

    }
    public ResponseEntity updateTopicMessage(TopicMessageDTO message) throws TopicMessageException{
        Message msg = new Message();
        msg = messageRepository.findById(message.getId()).orElse(null);
        if(message.getId()!=msg.getCreatedBy().getId()){
            return ResponseEntity.badRequest().build();
        }
        if(message.getTopic()!=msg.getTopic().getId()){
            return ResponseEntity.badRequest().build();
        }
        msg.setText(message.getText());
        try{
            messageRepository.save(msg);
            return ResponseEntity.ok(msg);
        }catch (Exception e) {
            throw new TopicMessageException("Ошибка редактирования темы");
        }
    }
    public ResponseEntity deleteTopicMessage(TopicMessageDTO message) throws TopicMessageException{
        Message msg = new Message();
        msg = messageRepository.findById(message.getId()).orElse(null);
        if(message.getId()!=msg.getCreatedBy().getId()){
            return ResponseEntity.badRequest().build();
        }
        try{
            messageRepository.delete(msg);
            return ResponseEntity.ok("Удаление успешно");
        }catch (Exception e) {
            throw new TopicMessageException("Ошибка удаления темы");
        }
    }
}
