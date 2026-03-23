package com.javadev.jobportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginRequestDTO {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(min=6,message = "Password must be at least 6 characters")
    private String password;
}
