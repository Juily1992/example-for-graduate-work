package ru.skypro.homework.dto;

//  отвечает за передачу данных при смене пароля пользователя в приложении.

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class NewPassword {
    @NotBlank
    @Size(min = 8, max = 16)
    private String currentPassword;

    @NotBlank
    @Size(min = 8, max = 16)
    private String newPassword;
}
