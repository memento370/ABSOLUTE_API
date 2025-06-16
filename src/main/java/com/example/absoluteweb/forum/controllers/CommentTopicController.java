package com.example.absoluteweb.forum.controllers;

import com.example.absoluteweb.forum.DTO.CommentTopicDTO;
import com.example.absoluteweb.forum.exceptions.CommentTopicException;
import com.example.absoluteweb.forum.principals.UserPrincipal;
import com.example.absoluteweb.forum.services.CommentTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forum/comment-topic")
public class CommentTopicController {

    private final CommentTopicService commentTopicService;

    @Autowired
    public CommentTopicController(CommentTopicService commentTopicService) {
        this.commentTopicService = commentTopicService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentTopicDTO commentDTO,
                                           @AuthenticationPrincipal UserPrincipal principal) {
        try {
            return commentTopicService.createComment(commentDTO, principal);
        } catch (CommentTopicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id,
                                           @RequestBody CommentTopicDTO commentDTO,
                                           @AuthenticationPrincipal UserPrincipal principal) {
        try {
            return commentTopicService.updateComment(id, commentDTO, principal);
        } catch (CommentTopicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        try {
            commentTopicService.deleteComment(id, principal);
            return ResponseEntity.noContent().build();
        } catch (CommentTopicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<List<CommentTopicDTO>> getCommentsByTopic(@PathVariable Long topicId) {
        try {
            return commentTopicService.getCommentsByTopic(topicId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
