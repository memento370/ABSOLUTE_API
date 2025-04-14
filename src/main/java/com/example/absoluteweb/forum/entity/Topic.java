package com.example.absoluteweb.forum.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    private LocalDateTime creationDate;

    private String section;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;


    public Topic() {
    }

    public Topic(String status, User createdBy, LocalDateTime creationDate, String section, String title, String message) {
        this.status = status;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.section = section;
        this.title = title;
        this.message = message;
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
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
}
