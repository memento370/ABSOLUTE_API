package com.example.absoluteweb.site.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SiteVerificationRequest {

    @NotBlank
    private String login;

    @NotBlank
    @Email
    private String l2email;

    @NotBlank
    private String password;

    @NotBlank
    private String code;

    // Getters & setters
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getL2email() { return l2email; }
    public void setL2email(String l2email) { this.l2email = l2email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}