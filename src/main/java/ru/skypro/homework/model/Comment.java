package ru.skypro.homework.model;

//представляет комментарий к объявлению в базе данных, работает с этой таблицей (добавлять, удалять, искать комментарии).
//Используется при сохранении, чтении, обновлении и удалении комментариев через CommentRepository

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Ad ad;

    @ManyToOne
    private User author;

    private String text;
    private LocalDateTime createdAt = LocalDateTime.now();
}