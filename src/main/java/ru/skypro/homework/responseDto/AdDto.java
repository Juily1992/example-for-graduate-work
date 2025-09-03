package ru.skypro.homework.responseDto;

// определяет, как будет выглядеть ответ сервера, когда фронтенд запрашивает список объявлений

import lombok.Data;

@Data
public class AdDto {
    private Integer author; // id автора
    private String image;
    private Integer pk;     // id объявления
    private Integer price;
    private String title;
}
