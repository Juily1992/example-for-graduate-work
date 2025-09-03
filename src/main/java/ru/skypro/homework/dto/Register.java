package ru.skypro.homework.dto;

// отвечает за получение и валидацию данных при регистрации нового пользователя

import lombok.Data;
import jakarta.validation.constraints.*;


@Data
public class Register {
    @NotBlank
    @Size(min = 4, max = 32)
    private String username;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;

    @NotBlank
    @Size(min = 2, max = 16)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 16)
    private String lastName;

    @Pattern(regexp = "\\+7\\s?\\(\\d{3}\\)\\s?\\d{3}-\\d{2}-\\d{2}")
    private String phone;

    private Role role = Role.USER; // по умолчанию USER
}
