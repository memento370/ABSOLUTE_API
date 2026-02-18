package com.example.absoluteweb.site.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmailChangeConfirmDTO {
    @NotBlank(message = "{site.email.change.code.required}")
    private String code;

    @NotBlank(message = "{site.email.change.newEmail.required}")
    @Email(message = "{site.email.change.newEmail.invalid}")
    @Size(max = 254, message = "{site.email.change.newEmail.max.length}")
    private String newEmail;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getNewEmail() { return newEmail; }
    public void setNewEmail(String newEmail) { this.newEmail = newEmail; }
}
