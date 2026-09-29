package com.mathieup.store.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotNull(message = "Email must be provided")
    @Email(message = "Must be a valid email")
    private String email;
    @NotNull(message = "Password must be provided")
    private String password;
}
