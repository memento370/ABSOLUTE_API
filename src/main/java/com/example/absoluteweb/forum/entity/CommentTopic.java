package com.example.absoluteweb.forum.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "comment_topic")
public class CommentTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;

    public CommentTopic() {
    }

    public CommentTopic(User createdBy, Topic topic, String text, OffsetDateTime creationDate) {
        this.createdBy = createdBy;
        this.topic = topic;
        this.text = text;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(OffsetDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
