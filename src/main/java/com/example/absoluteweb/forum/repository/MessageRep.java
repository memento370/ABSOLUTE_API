package com.example.absoluteweb.forum.repository;

import com.example.absoluteweb.forum.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRep extends JpaRepository<Message, Long> {
    List<Message> findByTopicId(Long topicId);
}
