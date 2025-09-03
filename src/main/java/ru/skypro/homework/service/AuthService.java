package ru.skypro.homework.service;

import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.responseDto.JwtResponse;

public interface AuthService {
    boolean login(String userName, String password);

    JwtResponse login(Login login);

    void register(Register register);
}
