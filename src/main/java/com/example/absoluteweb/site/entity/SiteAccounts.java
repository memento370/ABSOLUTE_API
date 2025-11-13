package com.example.absoluteweb.site.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "accounts", uniqueConstraints = {
        @UniqueConstraint(columnNames = "login"),
        @UniqueConstraint(columnNames = "l2email")
})
public class SiteAccounts {
    @Id
    @Column(name = "login")
    @NotBlank(message = "site.login.required")
    @Size(max = 14, message = "site.login.max.length")
    private String login;

    @Column(name = "password")
    @NotBlank(message = "site.password.required")
    private String password;

    @Column(name = "l2email")
    @NotBlank(message = "site.l2email.required")
    @Email(message = "site.l2email.invalid")
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
