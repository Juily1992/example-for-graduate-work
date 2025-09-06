package ru.skypro.homework.dto;

// используется для передачи данных от фронтенда к бэкенду при попытке входа пользователя в систему

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Login {
    @NotBlank
    @Size(min = 4, max = 32)
    private String username;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;
}
