package com.example.absoluteweb.site.repository;

import com.example.absoluteweb.site.entity.SiteAccounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteAccountsRep extends JpaRepository<SiteAccounts, String> {
    SiteAccounts findByLogin (String login);
    SiteAccounts findByL2email (String l2email);

}
