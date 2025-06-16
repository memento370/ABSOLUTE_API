package com.example.absoluteweb.forum.repository;

import com.example.absoluteweb.forum.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRep extends JpaRepository<Topic, Long> {
    List<Topic> findBySubSection(String subsection);
}
