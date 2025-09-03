package ru.skypro.homework.responseDto;

// используется для отправки информации о пользователе от бэкенда к фронтенду через API

import lombok.Data;

@Data
public class UserDto {
    private Integer id;
    private String email; // username
    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    private String image;
}
