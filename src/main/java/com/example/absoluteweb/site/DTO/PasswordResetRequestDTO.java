package com.example.absoluteweb.site.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PasswordResetRequestDTO {
    @NotBlank(message = "site.email.required")
    @Email(message = "site.email.invalid")
    private String email;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
