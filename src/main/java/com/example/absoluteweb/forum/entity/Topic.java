package com.example.absoluteweb.forum.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    // Користувач, що створив тему
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "sub_section")
    private String subSection;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;
    @JsonIgnore
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentTopic> comments = new ArrayList<>();
    public Topic() {
    }

    public Topic(String status, User createdBy, LocalDateTime creationDate, String subSection, String title, String message) {
        this.status = status;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.subSection = subSection;
        this.title = title;
        this.message = message;
        this.comments = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getSubSection() {
        return subSection;
    }

    public void setSubSection(String subSection) {
        this.subSection = subSection;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CommentTopic> getComments() {
        return comments;
    }

    public void setComments(List<CommentTopic> comments) {
        this.comments = comments;
    }
}
