package ru.skypro.homework.dto;

// отвечает за передачу данных при создании или обновлении комментария в приложении

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateOrUpdateComment {
    @NotBlank
    @Size(min = 8, max = 64)
    private String text;
}
