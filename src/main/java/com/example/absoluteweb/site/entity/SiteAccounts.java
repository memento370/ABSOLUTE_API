package com.example.absoluteweb.site.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class SiteAccounts {
    @Id
    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "l2email")
    private String l2email;

    private String role;
    public SiteAccounts() {
    }

    public SiteAccounts(String login, String password, String l2email,String role) {
        this.login = login;
        this.password = password;
        this.l2email = l2email;
        this.role=role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getL2email() {
        return l2email;
    }

    public void setL2email(String l2email) {
        this.l2email = l2email;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
