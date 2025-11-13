package com.example.absoluteweb.site.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SiteRegistrationRequest {

    private String login;
    @NotBlank(message = "site.password.required")
    @Size(max = 14, message = "site.password.max.length")
    private String password;
    private String l2email;


    public SiteRegistrationRequest() {}

    public SiteRegistrationRequest(String login, String password, String l2email) {
        this.login = login;
        this.password = password;
        this.l2email = l2email;
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
}
