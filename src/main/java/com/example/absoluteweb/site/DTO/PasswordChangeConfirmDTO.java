package com.example.absoluteweb.site.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordChangeConfirmDTO {

    @NotBlank(message = "site.password.change.newPassword.required")
    @Size(max = 14, message = "site.password.change.newPassword.max.length")
    private String newPassword;

    @NotBlank(message = "site.password.change.code.required")
    @Size(min = 6, max = 6, message = "site.password.change.code.size")
    private String code;

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
