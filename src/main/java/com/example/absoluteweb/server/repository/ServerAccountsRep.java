package com.example.absoluteweb.server.repository;

import com.example.absoluteweb.server.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerAccountsRep extends JpaRepository<Accounts, String> {
    Accounts findByLogin (String login);
    Accounts findByL2email (String l2email);

}
