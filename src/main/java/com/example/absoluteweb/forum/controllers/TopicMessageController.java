package com.example.absoluteweb.forum.controllers;

import com.example.absoluteweb.forum.DTO.TopicMessageDTO;
import com.example.absoluteweb.forum.entity.Message;
import com.example.absoluteweb.forum.exceptions.TopicMessageException;
import com.example.absoluteweb.forum.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forum/messages")
public class TopicMessageController {

    private MessageService messageService;

    @Autowired
    public TopicMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/getTopicMessages/{topicId}")
    public ResponseEntity <List<Message>> getMessagesByTopic(@RequestBody Long topicId) {
        return messageService.getTopicMessages(topicId);
    }


    @PostMapping("/addNewTopicMessage")
    public ResponseEntity<TopicMessageDTO> createMessage(@RequestBody TopicMessageDTO message) {
        try{
            messageService.addTopicMessage(message);
            return ResponseEntity.ok(message);
        }catch (TopicMessageException e){
            return ResponseEntity.badRequest().body(message);
        }

    }

    @PutMapping("/updateTopicMessage/{id}")
    public ResponseEntity<TopicMessageDTO> updateMessage(@RequestBody TopicMessageDTO message) {
        try{
            messageService.updateTopicMessage(message);
            return ResponseEntity.ok(message);
        }catch (TopicMessageException e){
            return ResponseEntity.badRequest().body(message);
        }
    }

    @DeleteMapping("/deleteTopicMessage/{id}")
    public ResponseEntity<TopicMessageDTO> deleteMessage(@RequestBody TopicMessageDTO message) {
        try{
            messageService.deleteTopicMessage(message);
            return ResponseEntity.ok(message);
        }catch (TopicMessageException e){
            return ResponseEntity.badRequest().body(message);
        }
    }
}
