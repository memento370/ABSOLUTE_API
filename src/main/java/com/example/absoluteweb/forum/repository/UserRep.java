package com.example.absoluteweb.forum.repository;

import com.example.absoluteweb.forum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRep extends JpaRepository<User, Long> {
    User findByLogin(String login);

    User findByEmail(String email);

    User findByNick(String nick);
}
