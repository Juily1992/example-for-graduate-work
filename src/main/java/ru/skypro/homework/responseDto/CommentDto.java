package ru.skypro.homework.responseDto;

// отвечает за передачу данных о комментарии в нужном фронтенду формате

import lombok.Data;

@Data
public class CommentDto {
    private Integer author;         // id автора
    private String authorImage;     // ссылка на аватар
    private String authorFirstName; // имя автора
    private Long createdAt;         // в миллисекундах
    private Integer pk;             // id комментария
    private String text;
}
