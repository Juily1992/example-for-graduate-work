package ru.skypro.homework.responseDto;

// используется для отправки расширенной информации об одном конкретном объявлении на фронтенд

import lombok.Data;

@Data
public class ExtendedAdDto {
    private Integer pk;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private Integer price;
    private String title;
}