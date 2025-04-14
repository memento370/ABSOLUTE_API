package com.example.absoluteweb.forum.services;

import com.example.absoluteweb.forum.DTO.TopicDTO;
import com.example.absoluteweb.forum.entity.Topic;
import com.example.absoluteweb.forum.entity.User;
import com.example.absoluteweb.forum.exceptions.TopicEcception;
import com.example.absoluteweb.forum.repository.MessageRep;
import com.example.absoluteweb.forum.repository.TopicRep;
import com.example.absoluteweb.forum.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TopicService {

    private MessageRep messageRepository;

    private UserRep userRepository;

    private TopicRep topicRepository;

    @Autowired
    public TopicService(MessageRep messageRepository, UserRep userRepository, TopicRep topicRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    public ResponseEntity<Topic> getTopic(TopicDTO topic) throws TopicEcception {
        Topic topicFind = new Topic();
        topicFind = topicRepository.findById(topic.getId()).orElse(null);
        return ResponseEntity.ok(topicFind);
    }
    public ResponseEntity<Topic> createTopic(TopicDTO topicDTO) throws TopicEcception {
        Topic topic = new Topic();
        User user = userRepository.findById(topicDTO.getCreatedBy()).orElse(null);
        if (user == null) {
            throw new TopicEcception("Topic user null");
        }
        topic.setCreatedBy(user);
        topic.setCreationDate(LocalDateTime.now());
        try{
            topicRepository.save(topic);
            return ResponseEntity.ok(topic);
        }catch (TopicEcception e){
            return ResponseEntity.badRequest().build();
        }
    }
    public ResponseEntity<Topic> updateTopic(TopicDTO topicDTO) throws TopicEcception {
        Topic topic = topicRepository.findById(topicDTO.getId()).orElse(null);
        if (topic == null) {
            throw new TopicEcception("Topic not found");
        }
        if(topic.getCreatedBy().getId()!=topicDTO.getCreatedBy()){
            throw new TopicEcception("Topic creator not available");
        }
        topic.setStatus(topicDTO.getStatus());
        topic.setSection(topicDTO.getSection());
        topic.setTitle(topicDTO.getTitle());
        topic.setMessage(topicDTO.getMessage());
        try{
            topicRepository.save(topic);
            return ResponseEntity.ok(topic);
        }catch (TopicEcception e){
            return ResponseEntity.badRequest().build();
        }

    }
    public ResponseEntity<Topic> deleteTopic(TopicDTO topicDTO) throws TopicEcception {
        Topic topic = topicRepository.findById(topicDTO.getId()).orElse(null);
        if (topic == null) {
            throw new TopicEcception("Topic not found");
        }
        if(topic.getCreatedBy().getId()!=topicDTO.getCreatedBy()){
            throw new TopicEcception("Topic creator not available");
        }
        try{
            topicRepository.delete(topic);
            return ResponseEntity.ok(topic);
        }catch (TopicEcception e){
            return ResponseEntity.badRequest().build();
        }

    }
}
