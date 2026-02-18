package com.example.absoluteweb.site.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class PasswordResetConfirmDTO {

    @NotBlank(message = "site.email.required")
    @Email(message = "site.email.invalid")
    private String email;

    @NotBlank(message = "site.code.required")
    @Pattern(regexp = "\\d{6}", message = "site.code.invalid")
    private String code;

    @NotBlank(message = "site.password.required")
    @Size(max = 14, message = "site.password.max.length")
    private String newPassword;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
