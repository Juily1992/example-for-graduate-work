package ru.skypro.homework.dto;

// класс для передачи данных при обновлении информации об  авторизованном пользователе

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateUser {
    @NotBlank
    @Size(min = 3, max = 10)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 10)
    private String lastName;

    @Pattern(regexp = "\\+7\\s?\\(\\d{3}\\)\\s?\\d{3}-\\d{2}-\\d{2}")
    private String phone;
}
