package com.example.absoluteweb.site.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginChangeConfirmDTO {

    @NotBlank(message = "site.login.change.newLogin.required")
    @Size(min = 3, max = 14, message = "site.login.change.newLogin.size")
    private String newLogin;

    @NotBlank(message = "site.login.change.code.required")
    @Size(min = 6, max = 6, message = "site.login.change.code.size")
    private String code;

    public String getNewLogin() { return newLogin; }
    public void setNewLogin(String newLogin) { this.newLogin = newLogin; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
