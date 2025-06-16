package com.example.absoluteweb.forum.repository;

import com.example.absoluteweb.forum.entity.CommentTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentTopicRep extends JpaRepository<CommentTopic, Long> {
    List<CommentTopic> findByTopicId(Long topicId);
}
