package ru.skypro.homework.model;

//отвечает за представление таблицы объявлений c колонками  в базе данных и за работу с данными объявления в коде
//

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private String title;
    private Integer price;
    private String description;
    private String image;
    private LocalDateTime createdAt = LocalDateTime.now();
}
