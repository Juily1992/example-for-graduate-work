package ru.skypro.homework.dto;

//используется для передачи информации при создании или обновлении объявления в приложении

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateOrUpdateAd {

    @NotBlank(message = "Title is required")
    @Size(min = 4, max = 32, message = "Title must be between 4 and 32 characters")
    private String title;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be at least 0")
    @Max(value = 10_000_000, message = "Price must be less than 10 million")
    private Integer price;

    @Size(min = 8, max = 64, message = "Description must be between 8 and 64 characters")
    private String description;
}