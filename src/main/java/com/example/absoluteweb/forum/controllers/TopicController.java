package com.example.absoluteweb.forum.controllers;

import com.example.absoluteweb.forum.DTO.TopicDTO;
import com.example.absoluteweb.forum.entity.Topic;
import com.example.absoluteweb.forum.repository.TopicRep;
import com.example.absoluteweb.forum.repository.UserRep;
import com.example.absoluteweb.forum.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forum/topic")
public class TopicController {

    @Autowired
    private TopicRep topicRepository;

    @Autowired
    private UserRep userRepository;
    @Autowired
    private TopicService topicService;

    @GetMapping("/getAllTopic")
    public List<Topic> getAllTopic() {
        return topicRepository.findAll();
    }

    @PostMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@RequestBody TopicDTO topic) {
        return topicService.getTopic(topic);
    }

    @PostMapping("/createTopic")
    public ResponseEntity<Topic> createTopic(@RequestBody TopicDTO topic) {
            return topicService.createTopic(topic);
    }

    @PutMapping("/updateTopic/{id}")
    public ResponseEntity<Topic> updateTopic(@RequestBody TopicDTO topic) {
        return topicService.updateTopic(topic);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Topic> deleteTopic(@RequestBody TopicDTO topic) {
        return topicService.deleteTopic(topic);
    }
}
