package com.example.absoluteweb.forum.controllers;

import com.example.absoluteweb.forum.DTO.TopicDTO;
import com.example.absoluteweb.forum.entity.Topic;
import com.example.absoluteweb.forum.principals.UserPrincipal;
import com.example.absoluteweb.forum.services.TopicService;
import com.example.absoluteweb.forum.exceptions.TopicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forum/topic")
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<?> createTopic(
            @RequestBody TopicDTO topicDTO,
            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            // Тут повертаємо вже DTO або мапу з id
            return topicService.createTopic(topicDTO, principal);
        } catch (TopicException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/by-section/{subSection}")
    public ResponseEntity<List<TopicDTO>> getTopicsBySection(@PathVariable String subSection) {
        return topicService.getTopicsBySection(subSection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDTO> getTopicById(@PathVariable Long id) {
        return topicService.getTopicById(id);
    }
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTopic(
            @PathVariable Long id,
            @RequestBody TopicDTO topicDTO,
            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            return topicService.updateTopic(id, topicDTO, principal);
        } catch (TopicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTopic(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            topicService.deleteTopic(id, principal);
            return ResponseEntity.ok().build();
        } catch (TopicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
